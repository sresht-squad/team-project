package com.example.restauranteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.models.Visit;

import java.util.ArrayList;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    ArrayList<Visit> mVisit;
    Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View VisitView = inflater.inflate(R.layout.item_visit, parent, false);
        ViewHolder viewHolder = new ViewHolder(VisitView);
        return viewHolder;

    }

    public void onBindViewHolder(ViewHolder holder, int position) {

        Visit visit = mVisit.get(position);

        holder.etActiveVisit.setText(visit.getObjectId());

    }

    @Override
    public int getItemCount() {
        return mVisit.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView etActiveVisit;


        public ViewHolder(View itemView){
            super(itemView);
            // connects with imageView
            etActiveVisit = itemView.findViewById(R.id.tvActiveVisit);


            }

        public void clear() {
            mVisit.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(ArrayList<Visit> list) {
            mVisit.addAll(list);
            notifyDataSetChanged();
        }


    }


}
