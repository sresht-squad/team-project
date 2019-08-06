package com.example.restauranteur.Server.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ServerRequestsFragment extends Fragment {
    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;

    public ServerRequestsFragment() {
        // Required empty public constructor
    }

    public static ServerRequestsFragment newInstance(int page, String title) {
        ServerRequestsFragment fragmentFirst = new ServerRequestsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server_requests, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("REFRESHING", "we are refreshing whoooo");
                //repopulate
                loadMessages();
                //now make sure swipeContainer.setRefreshing is set to false
                //but let's not do that here becauuuuuse.... ASYNCHRONOUS
                //lets put it at the end of populateTimeline instead!

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.lightBlueMaterialDesign,
                R.color.yellow,
                android.R.color.holo_red_light);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        rvChat = view.findViewById(R.id.rvChat);

        mMessages = new ArrayList<>();

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);

        loadMessages();
    }


    // Query messages from Parse so we can load them into the chat adapter
    private void loadMessages() {
        mAdapter = new ChatAdapter(getContext(), true, false, mMessages);
        rvChat.setAdapter(mAdapter);
        Log.i("DISPLAY", "ALL_MESSAGES");
        //for all visits that involve this server
        JSONArray visits = Server.getCurrentServer().getVisitsJSON();
        if (visits == null) {
            return;
        }
        if (mMessages != null) {
            mMessages.clear();
        }
        int visitNum = visits.length();
        String visitId;
        //lookup the pointers to get actual visits
        for (int i = 0; i < visitNum; i++) {
            try {
                visitId = visits.getJSONObject(i).getString("objectId");
                // look up this visit
                lookupVisit(visitId);
            } catch (JSONException e) {

            }
        }
        swipeContainer.setRefreshing(false);
    }


    private void lookupVisit(String visitId) {
        ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);
        query.whereEqualTo("objectId", visitId);
        query.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(List<Visit> objects, ParseException e) {
                if (e == null) {
                    // there is only one object since we are querying by object id
                    Visit visit = objects.get(0);
                    // find all the messages for this visit
                    findMessages(visit);
                }
            }
        });
    }


    private void findMessages(Visit thisVisit) {
        // get the array of pointers to messages
        List<ParseObject> messagePointers = thisVisit.getMessageList();
        try {
            ParseObject.fetchAllIfNeeded(messagePointers);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < messagePointers.size(); i++) {
            Message message = (Message) messagePointers.get(i);
            String tableNumber = thisVisit.getTableNumber();
            if (message.getActive()) {
                message.tableNum = tableNumber;
                mMessages.add(message);
            }
            if (mMessages.size() > 0) {
                mMessages.sort(new Comparator<Message>() {
                    public int compare(Message m1, Message m2) {
                        long diff = (m1.getCreatedAt().getTime() - m2.getCreatedAt().getTime());
                        if (diff > 0) {
                            return 1;
                        } else if (diff == 0) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });
            }
            mAdapter.notifyDataSetChanged();
        }

        if (getActivity() instanceof ServerHomeActivity) {
            final ServerHomeActivity homeActivity = (ServerHomeActivity) getActivity();
            homeActivity.addBadgeRequestView(mMessages.size());
            homeActivity.refreshRequestBadgeView(mMessages.size());
        }

    }
}