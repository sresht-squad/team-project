package com.example.restauranteur.Server.Fragment;

import android.os.Bundle;
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
import com.example.restauranteur.Server.Activity.VisitAdapter;
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

    ServerInfo info;

    private static ServerActiveVisitsFragment mActiveVisitsFragment;

    public ServerActiveVisitsFragment() {
        //required empty constructor
    }

    public static ServerActiveVisitsFragment newInstance(int page, String title) {
        if (mActiveVisitsFragment == null){
            mActiveVisitsFragment = new ServerActiveVisitsFragment();
        }
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        mActiveVisitsFragment.setArguments(args);
        return mActiveVisitsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_server_active_visit, parent, false);
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        visits = new ArrayList<>();
        rvActiveVisit = view.findViewById(R.id.rvActiveVisits);
        rvActiveVisit.setLayoutManager(new LinearLayoutManager(getContext()));
        visitAdapter = new VisitAdapter(visits);
        rvActiveVisit.setAdapter(visitAdapter);

       swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                visits.clear();
                visitAdapter.notifyDataSetChanged();
                queryServerInfo();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.lightBlueMaterialDesign,
                R.color.yellow,
                android.R.color.holo_red_light);

        queryServerInfo();
    }


   private void queryServerInfo(){
        final ParseQuery<ServerInfo> query = ParseQuery.getQuery(ServerInfo.class);
        query.whereEqualTo("objectId", Server.getCurrentServer().getServerInfo().getObjectId());

        query.findInBackground(new FindCallback<ServerInfo>() {
            @Override
            public void done(List<ServerInfo> objects, ParseException e) {
                info = objects.get(0);
                fetchActiveVisits();
            }
        });

    }


    private void fetchActiveVisits() {
        visits.addAll(info.getVisits());
        visitAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);

    }

}


