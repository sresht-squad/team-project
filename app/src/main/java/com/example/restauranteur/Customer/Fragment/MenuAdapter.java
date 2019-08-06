package com.example.restauranteur.Customer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
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
import static android.view.View.GONE;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<MenuItem> mMenuItems;
    Context context;


    public MenuAdapter(ArrayList<MenuItem> menuItems, Context context){
        this.context = context;
        mMenuItems= menuItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MenuItem item = mMenuItems.get(position);
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
        setAppearanceHeading(holder);
        holder.foodName.setTextSize(30);
     //   holder.foodName.setTextColor(0x61616161);
    }

    private void setAppearanceHeading(ViewHolder holder){
        holder.menuCardview.setCardBackgroundColor(0xFFFAFAFA);
        holder.description.setVisibility(GONE);
        holder.clMenuFade.setVisibility(GONE);
        holder.foodName.setGravity(CENTER);
        holder.foodName.setTextSize(24);
        holder.price.setVisibility(GONE);
       // holder.foodName.setTextColor(0x42424242);
    }

    private void setAppearanceFoodItem(ViewHolder holder, MenuItem item){
        holder.foodName.setGravity(LEFT);
        holder.clMenuFade.setVisibility(View.VISIBLE);
        holder.menuCardview.setCardBackgroundColor(0xFFFFFFFF);
        holder.foodName.setTextSize(16);
       // holder.foodName.setTextColor(0x9E9E9E9E);

        //Only show the description if there is one available
        if (item.getDescription() == null){
            holder.description.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
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
        CardView menuCardview;
        ConstraintLayout clMenu;
        ConstraintLayout clMenuFade;
        ConstraintLayout.LayoutParams defaultParams;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.tvPrice);
            menuCardview = itemView.findViewById(R.id.cardView);
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
                MenuItem item = mMenuItems.get(position);
                //Only non-headings are clickable
                if (!item.getHeading()) {
                    final String order = item.getName();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
                    builder.setNegativeButton("Cancel", null);
                    builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postMessage(order);
                        }
                    });
                    builder.setTitle(order).setMessage("Would you like to order 1 " + order + " ?").create().show();
                }
            }
        }
    }

    private void postMessage(String data) {
        final Message message = new Message();
        message.setBody(data);
        Customer customer = Customer.getCurrentCustomer();
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
                        notifyDataSetChanged();
                        if (context instanceof CustomerHomeActivity) {
                            ((CustomerHomeActivity)context).vpPager.getAdapter().notifyDataSetChanged();
                        }
                        Toast.makeText(context, "Order has been placed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}