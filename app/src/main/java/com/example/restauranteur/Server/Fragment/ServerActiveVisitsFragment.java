package com.example.restauranteur.Server.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.example.restauranteur.VisitAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ServerActiveVisitsFragment extends Fragment {

    ArrayList<Visit> visits;
    RecyclerView rvActiveVisit;
    VisitAdapter visitAdapter;
    private SwipeRefreshLayout swipeContainer;
    Manifest manifest;
    Handler handler;


    public ServerActiveVisitsFragment() {
        //required empty constructor
    }

    public static ServerActiveVisitsFragment newInstance(int page, String title) {
        ServerActiveVisitsFragment fragmentFirst = new ServerActiveVisitsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_server_active_visit, parent, false);
        // Defines the xml file for the fragment
        // Lookup the swipe container view
        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.

                fetchActiveVisits();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        visits = new ArrayList<Visit>();
        rvActiveVisit = view.findViewById(R.id.rvActiveVisits);
        rvActiveVisit.setLayoutManager(new LinearLayoutManager(getContext()));
        visitAdapter = new VisitAdapter(visits);
        rvActiveVisit.setAdapter(visitAdapter);


        fetchActiveVisits();

    }

   private void update() {
      final Runnable r = new Runnable() {
           public void run() {
               fetchActiveVisits();
                handler.postDelayed(this, 5000);
          }
      };
        handler.postDelayed(r, 5000);
   }


    private void fetchActiveVisits(){
        final ParseQuery<ServerInfo> query = ParseQuery.getQuery(ServerInfo.class);
        query.whereEqualTo("objectId", Server.getCurrentServer().getServerInfo().getObjectId());

        query.findInBackground(new FindCallback<ServerInfo>() {
            @Override
            public void done(List<ServerInfo> objects, ParseException e) {
                if (e == null){
                    visits.clear();
                    visitAdapter.notifyDataSetChanged();
                   final ServerInfo serverInfo = objects.get(0);

                   for (int i = 0 ; i < serverInfo.getVisits().size() ; i++) {

                       if (getActivity() instanceof ServerHomeActivity) {
                           final ServerHomeActivity homeActivity = (ServerHomeActivity) getActivity();
                           homeActivity.addBadgeActiveView(serverInfo.getVisits().size());
                           homeActivity.refreshActiveBadgeView(serverInfo.getVisits().size());
                       }

                       visits.add(serverInfo.getVisits().get(i));
                       visitAdapter.notifyDataSetChanged();


                   }
                } else {
                    e.printStackTrace();
                }
               swipeContainer.setRefreshing(false);
            }
        });
    }
}