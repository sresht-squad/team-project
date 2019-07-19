package com.example.restauranteur;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restauranteur.simpleChat.ChatAdapter;
import com.example.restauranteur.simpleChat.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;


public class ServerRequestsFragment extends Fragment {
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;

    private EditText etMessage;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;

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
        etMessage = view.findViewById(R.id.etMessage);
        rvChat = view.findViewById(R.id.rvChat);

        mMessages = new ArrayList<>();
        mFirstLoad = true;

        final String userId = getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(getContext(), userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);

        refreshMessages();
    }


    // Query messages from Parse so we can load them into the chat adapter
    private void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                Visit v;
                if (e == null) {
                    mMessages.clear();
                }
               // mMessages.addAll(messages);
                Message m;
                Server server;
                String serverId;
                String userId;
                int size = messages.size();
                //only show the messages for visits that involve the current logged-in server
               for (int i = 0; i < size; i++) {
                    m = messages.get(i);
                    v = (Visit) m.getVisit();
                    serverId = v.getServer().getObjectId();
                    userId = getCurrentUser().getObjectId();
                   if (serverId.equals(userId)) {
                        //Log.i(serverId, userId);
                        mMessages.add(m);
                    }
                }

                mAdapter.notifyDataSetChanged(); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    rvChat.scrollToPosition(0);
                    mFirstLoad = false;
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
}
