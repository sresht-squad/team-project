package com.example.restauranteur;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    private List<Visit> mVisits;
    private Context context;

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

        //get the table number
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
        return mVisits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvActiveVisit;
        TextView tvTableNumber;

        public ViewHolder(View itemView){
            super(itemView);
            // connects with imageView
            tvActiveVisit = itemView.findViewById(R.id.tvActiveVisit);
            tvTableNumber = itemView.findViewById(R.id.tvTableNumber);

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

    }


}
