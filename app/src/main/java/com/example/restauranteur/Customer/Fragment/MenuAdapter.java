package com.example.restauranteur.Customer.Fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.MenuItem;
import com.example.restauranteur.R;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
            if (mMenuItems.get(position).getHeading()){
                Log.i("HEADING", mMenuItems.get(position).getName());
            }
        MenuItem food = mMenuItems.get(position);
        holder.foodName.setText(food.getName());
        if (food.getPrice() == null){
            holder.price.setVisibility(GONE);
        }
        if (mMenuItems.get(position).getHeading()){
            holder.foodName.setGravity(CENTER);
            holder.foodName.setTextSize(20);
            holder.description.setVisibility(GONE);
        } else{
            holder.description.setText(food.getDescription());
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

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.tvPrice);

        }
    }

}