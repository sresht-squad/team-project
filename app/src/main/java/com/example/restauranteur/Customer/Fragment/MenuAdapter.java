package com.example.restauranteur.Customer.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
import com.example.restauranteur.Customer.Activity.CustomerNewVisitActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.MenuItem;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.EventListener;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<MenuItem> mMenuItems;
    Context context;
    ViewGroup myParent;

    public interface EventListener {
        void onEvent(int data);
    }

    EventListener listener;


    MenuAdapter(ArrayList<MenuItem> menuItems, EventListener listener) {
        mMenuItems= menuItems;
        this.listener = listener;

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
        if (item.getPrice() == null){
            holder.price.setVisibility(View.GONE);
        } else {
            holder.price.setText(item.getPrice());
            holder.price.setVisibility(View.VISIBLE);
        }

        if (item.getDescription() == null){
            holder.description.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            holder.description.setLayoutParams(layoutParams);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setLayoutParams(holder.defaultParams);
        }

        if (item.getHeading()){
            Log.i("HEADING", mMenuItems.get(position).getName());
            holder.menuCardview.setCardBackgroundColor(0xFFFAFAFA);
            holder.clMenuFade.setVisibility(GONE);
            holder.foodName.setTextAppearance(android.R.style.TextAppearance_Large);
            holder.foodName.setGravity(CENTER);
            //holder.foodName.setTextSize(20);
            holder.description.setVisibility(View.INVISIBLE);
            if (item.getMainHeading()){
                holder.foodName.setTextAppearance(android.R.style.TextAppearance_Material_Display1);
            }
        } else{
            holder.foodName.setGravity(LEFT);
            holder.clMenuFade.setVisibility(View.VISIBLE);
            holder.foodName.setTextAppearance(android.R.style.TextAppearance_Small);
            holder.menuCardview.setCardBackgroundColor(0xFFFFFFFF);
            holder.foodName.setTextSize(16);
            holder.description.setText(item.getDescription());
            holder.description.setVisibility(View.VISIBLE);
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
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                MenuItem item = mMenuItems.get(position);
                String order = item.getName();
                Toast.makeText(context, order, Toast.LENGTH_LONG).show();
              //  EventListener.onEvent();
                //AboutBox.Show((CustomerHomeActivity) context.getClass());
                postMessage(order);
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
                Toast.makeText(context, "Created message on Parse", Toast.LENGTH_SHORT).show();
                visit.addMessage(message);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
               //         Toast.makeText(this, "Message added to visit", Toast.LENGTH_SHORT).show();
                    }
                });
                notifyDataSetChanged();
            }
        });
    }

}