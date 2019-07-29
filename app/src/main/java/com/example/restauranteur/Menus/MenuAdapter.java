package com.example.restauranteur.Menus;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Message;
import com.example.restauranteur.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.List;

import static android.os.SystemClock.sleep;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<JSONObject> mMenuItems;
    private String mUserId;
    Boolean serverPage;

    public MenuAdapter(Context context, Boolean serverScreen, String userId, List<JSONObject> menuItems) {
        mMenuItems= menuItems;
        serverPage = serverScreen;
        this.mUserId = userId;
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
        JSONObject object = mMenuItems.get(position);
      //  holder.foodName.setText(object.getBody());
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
        }
    }

}