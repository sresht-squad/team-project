package com.example.restauranteur.Customer.Fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.MenuItem;
import com.example.restauranteur.R;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private ArrayList<MenuItem> mMenuItems;

    MenuAdapter(ArrayList<MenuItem> menuItems) {
        mMenuItems= menuItems;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_menu, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int p) {
            int position = holder.getAdapterPosition();
        MenuItem item = mMenuItems.get(position);
        holder.foodName.setText(item.getName());
        if (item.getPrice() == null){
            holder.price.setVisibility(GONE);
        }
        if (item.getHeading()){
            Log.i("HEADING", mMenuItems.get(position).getName());
          //  holder.menuCardview.setCardBackgroundColor();
            holder.foodName.setGravity(CENTER);
            holder.foodName.setTextSize(20);
            holder.description.setVisibility(GONE);
        } else{
            holder.foodName.setGravity(LEFT);
            holder.foodName.setTextSize(16);
            holder.description.setText(item.getDescription());
            holder.description.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;
        TextView description;
        TextView price;
        CardView menuCardview;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.tvPrice);
            menuCardview = itemView.findViewById(R.id.cardView);

        }
    }

}