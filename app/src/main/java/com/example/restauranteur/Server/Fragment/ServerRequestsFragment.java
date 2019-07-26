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
import java.util.Comparator;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;


public class ServerRequestsFragment extends Fragment {
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;

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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
        List<Visit> visits2 = Server.getCurrentServer().getServerInfo().getVisits2();
        for (int i = 0; i < visits2.size(); i++) {
            findMessages(visits2.get(i));
        }
    }

    private void findMessages(Visit thisVisit) {
        // get the array of pointers to messages

       // JSONArray messagePointers = thisVisit.getMessages();
        ArrayList<Message> messagesList = thisVisit.getMessageList();
        for (int i = 0; i < messagesList.size(); i++) {
            Message message = messagesList.get(i);
            if (message.getActive()) {
                String tableNumber = thisVisit.getTableNumber();
                message.tableNum = tableNumber;
                mMessages.add(message);

                if (mMessages.size() > 0) {
                    mMessages.sort(new Comparator<Message>() {
                        public int compare(final Message m1, final Message m2) {
                            final long diff = (m1.getCreatedAt().getTime() - m2.getCreatedAt().getTime());
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

        }
    }
}