package com.example.restauranteur.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.R;
import com.example.restauranteur.VisitAdapter;
import com.example.restauranteur.models.Visit;

import java.util.ArrayList;

public class ServerActiveVisitsFragment extends Fragment {

    ArrayList<Visit> visit;
    RecyclerView rvActiveVisit;
    VisitAdapter visitAdapter;

    public ServerActiveVisitsFragment(){
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
        visit = new ArrayList<>();
        rvActiveVisit = view.findViewById(R.id.rvActiveVisits);
        rvActiveVisit.setLayoutManager(new LinearLayoutManager(getContext()));
        visitAdapter = new VisitAdapter(visit);
        rvActiveVisit.setAdapter(visitAdapter);

        fetchActiveVisit();

    }

    private void fetchActiveVisit() {

    }

}
