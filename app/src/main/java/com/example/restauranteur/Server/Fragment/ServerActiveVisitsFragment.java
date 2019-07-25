package com.example.restauranteur.Server.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.VisitAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ServerActiveVisitsFragment extends Fragment {

    ArrayList<Visit> visit;
    RecyclerView rvActiveVisit;
    VisitAdapter visitAdapter;

    public ServerActiveVisitsFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_server_active_visit, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        visit = new ArrayList<Visit>();
        rvActiveVisit = view.findViewById(R.id.rvActiveVisits);
        rvActiveVisit.setLayoutManager(new LinearLayoutManager(getContext()));
        visitAdapter = new VisitAdapter(visit);
        rvActiveVisit.setAdapter(visitAdapter);

        fetchActiveVisits();

    }

   /* private void fetchActiveVisit() {
        final ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);
        query.whereEqualTo("active", true);

        // Specify the object id
        query.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(List<Visit> objects, ParseException e) {
                if (e == null) {
                    visit.clear();
                    visitAdapter.notifyDataSetChanged();
                    for (int i = 0; i < objects.size(); i++) {
                        Visit OneVisit = objects.get(i);
                        String serverId = OneVisit.getServer().getObjectId();
                        if (serverId.equals(Server.getCurrentServer().getObjectId())) {
                            visit.add(OneVisit);

                            visitAdapter.notifyItemInserted(visit.size() - 1);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private void fetchActiveVisits(){
        final ParseQuery<ServerInfo> query = ParseQuery.getQuery(ServerInfo.class);
        query.whereEqualTo("objectId", Server.getCurrentServer().getServerInfo().getObjectId());

        query.findInBackground(new FindCallback<ServerInfo>() {
            @Override
            public void done(List<ServerInfo> objects, ParseException e) {
                if (e == null){
                    visit.clear();
                    visitAdapter.notifyDataSetChanged();

                   ServerInfo serverInfo = objects.get(0);


                   for (int i = 0 ; i < serverInfo.getVisit().size() ; i++){
                       visit.add(serverInfo.getVisit().get(i));
                       visitAdapter.notifyDataSetChanged();

                   }

                } else {
                    e.printStackTrace();
                }
            }
        });
    }


}