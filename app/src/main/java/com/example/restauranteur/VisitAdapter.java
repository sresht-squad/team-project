package com.example.restauranteur;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    List<Visit> mVisit;
    Context context;

    public VisitAdapter(List<Visit> mVisit) {
        this.mVisit = mVisit;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View VisitView = inflater.inflate(R.layout.item_visit, parent, false);
        ViewHolder viewHolder = new ViewHolder(VisitView);
        return viewHolder;

    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        Visit visit = mVisit.get(position);

        holder.etActiveVisit.setText(visit.getObjectId());

        final ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);
        query.whereEqualTo("objectId", visit.getObjectId());

        query.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(List<Visit> objects, ParseException e) {
                Visit singleVisit = objects.get(0);
                holder.tvTableNumber.setText(singleVisit.getTableNumber());
            }
        });


    }




    @Override
    public int getItemCount() {
        return mVisit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView etActiveVisit;
        TextView tvTableNumber;
        CheckBox cbDone;

        public ViewHolder(View itemView){
            super(itemView);
            // connects with imageView
            etActiveVisit = itemView.findViewById(R.id.tvActiveVisit);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);
            cbDone = itemView.findViewById(R.id.cbDone);

            cbDone.setChecked(false);
            cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        // search for that visit's table number and server to get the specific visit
                        final ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);
                        query.whereEqualTo("server", Server.getCurrentServer().getParseUser());

                        query.findInBackground(new FindCallback<Visit>() {
                            @Override
                            public void done(List<Visit> objects, ParseException e) {
                                if (e == null){
                                    for (int i = 0; i < objects.size(); i++){
                                        Visit visit = objects.get(i);
                                        if (visit.getTableNumber().equals(tvTableNumber.getText().toString())){
                                            // change boolean true to false.Visit is not Active
                                            visit.setActive(false);
                                            visit.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Log.i("saved", "visit Active false");
                                                }
                                            });

                                        }
                                    }
                                }else{
                                    e.printStackTrace();
                                }

                            }
                        });
                        Log.i("checkBox Done", tvTableNumber.getText().toString());
                    }else{
                        Log.i("checBox Done", "unChecked");

                    }
                }
            });


        }

        public void clear() {
            mVisit.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Visit> list) {
            mVisit.addAll(list);
            notifyDataSetChanged();
        }



    }




}
