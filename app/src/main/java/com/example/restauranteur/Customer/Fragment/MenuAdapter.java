package com.example.restauranteur.Customer.Fragment;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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




    class ViewHolder extends RecyclerView.ViewHolder{
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

        }
    }

}