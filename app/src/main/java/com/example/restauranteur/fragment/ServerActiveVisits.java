package com.example.restauranteur.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.VisitAdapter;
import com.example.restauranteur.models.Visit;

import java.util.ArrayList;

public class ServerActiveVisits extends Fragment {

    ArrayList<Visit> visits;
    RecyclerView rvActiveVisit;
    VisitAdapter visitAdapter;

}
