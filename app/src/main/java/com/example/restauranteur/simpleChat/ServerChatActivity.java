package com.example.restauranteur.simpleChat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.R;
import com.example.restauranteur.Visit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;


public class ServerChatActivity extends AppCompatActivity {
    static final String TAG = ServerChatActivity.class.getSimpleName();
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    EditText etMessage;
    Button btSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_chat);
        startWithCurrentUser();
        refreshMessages();
    }


    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        rvChat = findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(ServerChatActivity.this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ServerChatActivity.this);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();

                // Using new `Message` Parse-backed model now
                Message message = new Message();
                message.setBody(data);
                message.setAuthor(getCurrentUser());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ServerChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });
    }

        // Query messages from Parse so we can load them into the chat adapter
        void refreshMessages() {
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
                        //only show the messages for visits that involve the current logged-in server
                        for (int i = 0; i < messages.size(); i++){
                            Message m = messages.get(i);
                            v = (Visit) m.getVisit();
                            ParseUser s = v.getServer();
                            String serverId = s.getObjectId();
                            String userId = getCurrentUser().getObjectId();
                            if (serverId.equals(userId))
                            {
                                Log.i(serverId, userId);
                                mMessages.add(m);
                            }
                        }
                        mAdapter.notifyDataSetChanged(); // update adapter
                        // Scroll to the bottom of the list on initial load
                        if (mFirstLoad) {
                            rvChat.scrollToPosition(0);
                            mFirstLoad = false;
                        }
                    } else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });
        }
}
