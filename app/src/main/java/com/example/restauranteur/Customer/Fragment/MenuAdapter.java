package com.example.restauranteur.Customer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
import com.example.restauranteur.LoginSignup.SignupActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.MenuItem;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.START;
import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_LONG;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<MenuItem> mMenuItems;
    private Context context;
    private int bigHeadingCounter;


    MenuAdapter(ArrayList<MenuItem> menuItems, Context context){
        this.context = context;
        mMenuItems= menuItems;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View contactView = inflater.inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MenuItem item = mMenuItems.get(position);
        holder.foodName.setText(item.getName());

        if (item.getHeading()){
            if (item.getMainHeading()){
                setAppearanceBigHeading(holder);
            } else{
                setAppearanceHeading(holder);
            }
        } else{
            setAppearanceFoodItem(holder, item);
        }
    }

    private void setAppearanceBigHeading(ViewHolder holder){
        if (CustomerMenuFragment.menus.length() > 1){
            setAppearanceHeading(holder);
            holder.foodName.setTextSize(28);
        }
        else{
            holder.menuCardView.setVisibility(GONE);
            holder.clMenuFade.setVisibility(GONE);
            holder.clMenu.setVisibility(GONE);
        }
    }

    private void setAppearanceHeading(ViewHolder holder){
        holder.menuCardView.setCardBackgroundColor(0xFFFAFAFA);
        holder.description.setVisibility(GONE);
        holder.clMenuFade.setVisibility(GONE);
        holder.foodName.setGravity(CENTER);
        holder.foodName.setTextSize(24);
        holder.price.setVisibility(GONE);
    }

    private void setAppearanceFoodItem(ViewHolder holder, MenuItem item){
        holder.foodName.setGravity(START);
        holder.clMenuFade.setVisibility(View.VISIBLE);
        holder.menuCardView.setCardBackgroundColor(0xFFFFFFFF);
        holder.foodName.setTextSize(16);

        //Only show the description if there is one available
        if (item.getDescription() == null){
            holder.description.setVisibility(View.GONE);
            final ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            holder.description.setLayoutParams(layoutParams);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.getDescription());
            holder.description.setLayoutParams(holder.defaultParams);
        }

        //Only show the price if there is a price available
        if (item.getPrice() == null){
            holder.price.setVisibility(View.GONE);
        } else {
            holder.price.setText(item.getPrice());
            holder.price.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView foodName;
        TextView description;
        TextView price;
        CardView menuCardView;
        ConstraintLayout clMenu;
        ConstraintLayout clMenuFade;
        ConstraintLayout.LayoutParams defaultParams;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.tvPrice);
            menuCardView = itemView.findViewById(R.id.cardView);
            clMenu = itemView.findViewById(R.id.clMenuItem);
            clMenuFade = itemView.findViewById(R.id.constraintLayout2);
            defaultParams = (ConstraintLayout.LayoutParams) description.getLayoutParams();
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //When an item is clicked, a box will pop up prompting you to place the order or cancel
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final MenuItem item = mMenuItems.get(position);
                //Only non-headings are clickable
                if (!item.getHeading()) {
                    final String order = item.getName();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder.setNegativeButton("Cancel", null);
                    builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postMessage(order);
                        }
                    });
                    builder.setTitle(order).setMessage("Would you like to request an order of " + order + " ?").create().show();
                }
            }
        }
    }

    private void postMessage(String data) {
        final Message message = new Message();
        message.setBody(data);
        final Customer customer = Customer.getCurrentCustomer();
        final Visit visit = customer.getCurrentVisit();
        message.setAuthor(customer);
        message.setActive(true);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //if the message saves successfully, save it to the visit as well
                visit.addMessage(message);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(context, "Order has been placed", LENGTH_LONG).show();
                        notifyDataSetChanged();
                        if (context instanceof CustomerHomeActivity) {
                            ((CustomerHomeActivity)context).vpPager.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}