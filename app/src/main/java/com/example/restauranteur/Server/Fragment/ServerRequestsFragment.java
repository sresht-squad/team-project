package com.example.restauranteur.Server.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.R;
import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;


public class ServerRequestsFragment extends Fragment {
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private ArrayList<Visit> visits;
    private ChatAdapter mAdapter;
    private String tableNum;

    public ServerRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        // Find the text field and button
        rvChat = view.findViewById(R.id.rvChat);

        mMessages = new ArrayList<>();

        final String userId = getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(getContext(), true, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);

        loadMessages();
    }


    // Query messages from Parse so we can load them into the chat adapter
    private void loadMessages() {
        Log.i("DISPLAY", "ALL_MESSAGES");
        //for all visits that involve this server
        JSONArray visits = Server.getCurrentServer().getVisitsJSON();
        if (visits == null) {
            return;
        }
        int visitNum = visits.length();
        String visitId = "";
        //lookup the pointers to make an array of the visits
        for (int i = 0; i < visitNum; i++) {
            try {
                visitId = visits.getJSONObject(i).getString("objectId");
                lookupVisit(visitId);
            } catch (JSONException e) {

            }
        }
    }


     void lookupVisit(String visitId) {
            ParseQuery<Visit> query = ParseQuery.getQuery(Visit.class);
            query.whereEqualTo("objectId", visitId);
            query.findInBackground(new FindCallback<Visit>() {
                @Override
                public void done(List<Visit> objects, ParseException e) {
                    if (e == null) {
                        Visit visit = objects.get(0);
                        tableNum = visit.getTableNumber();
                        findMessages(visit);
                    }
                }
            });
    }


    void findMessages(Visit visit){
        JSONArray messagePointers = visit.getMessages();
        for (int i = 0; i < messagePointers.length(); i++){
            try {
                String messageId = messagePointers.getJSONObject(i).getString("objectId");
                extractMessages(messageId);
            } catch (JSONException e){

            }
        }


    }

    void extractMessages(String messageId) {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("objectId", messageId);
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) {
                    Message message = objects.get(0);
                    message.tableNum = tableNum;
                    mMessages.add(message);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
