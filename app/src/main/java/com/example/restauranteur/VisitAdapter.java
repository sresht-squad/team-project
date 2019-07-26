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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    private List<Visit> mVisits;
    private Context context;
    public String tableNum;

    public VisitAdapter(List<Visit> mVisit) {
        this.mVisits = mVisit;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View VisitView = inflater.inflate(R.layout.item_visit, parent, false);
        return new ViewHolder(VisitView);

    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Visit visit = mVisits.get(position);
        holder.tvTableNumber.setText(visit.getTableNumber());
        //get the objectId of the first customer attached to the visit
        String customerObjectId = null;
        try {
            customerObjectId = visit.getCustomers().getJSONObject(0).getString("objectId");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //query for the customer with that objectId to get their name
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("objectId", customerObjectId);

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                //get the first name
                final String customerName = objects.get(0).getString("firstName");

                //format it like so: Name (number in party)
                final int numCustomers = visit.getJSONArray("customers").length();
                final String nameText = customerName + " (" + numCustomers +")";
                holder.tvActiveVisit.setText(nameText);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVisits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvActiveVisit;
        TextView tvTableNumber;
        CheckBox cbDone;

        public ViewHolder(View itemView){
            super(itemView);
            // connects with imageView
            tvActiveVisit = itemView.findViewById(R.id.tvActiveVisit);
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
            mVisits.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Visit> list) {
            mVisits.addAll(list);
            notifyDataSetChanged();
        }

        public void removeAt(int position) {
            mVisits.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mVisits.size());
        }


    }


}
