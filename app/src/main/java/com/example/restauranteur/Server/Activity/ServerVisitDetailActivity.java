package com.example.restauranteur.Server.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.parse.ParseUser.getCurrentUser;

public class ServerVisitDetailActivity extends AppCompatActivity {
    private ArrayList<Message> mMessages;
    private ArrayList<Visit> visits;
    private ChatAdapter mAdapter;
    private String tableNum;
    private Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_visit_detail);

        final Intent intent = getIntent();
        visit = intent.getParcelableExtra("VISIT");
        String nameText = intent.getStringExtra("NAME_TEXT");

        //id lookups
        RecyclerView rvChat = (RecyclerView) findViewById(R.id.rvChat);
        TextView tvTableNum = (TextView) findViewById(R.id.tvTitle);

        tvTableNum.setText(nameText + ", Table " + visit.getTableNumber());


        mMessages = new ArrayList<>();

        final String userId = getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(this, true, true, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(linearLayoutManager);

        displayCurrentMessages();
    }

    private void displayCurrentMessages() {
        Log.i("DISPLAY", "ALL_MESSAGES");
        JSONArray messages = visit.getMessages();
        if (mMessages != null) {
            mMessages.clear();
        }
        int length = 0;
        if (messages != null) {
            length = messages.length();
        }

        //only show active messages created by the specific customer during this visit
        String messagePointer = "";
        //for all messages in the visit array
        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        for (int i = 0; i < length; i++) {

            ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
            //get pointer to message from JSON data
            try {
                messagePointer = messages.getJSONObject(i).getString("objectId");
                Log.i("MESSAGE_ID", messagePointer);
            } catch (JSONException e) {
                Log.i("MESS_ERROR", messagePointer);
            }
            query.whereEqualTo("objectId", messagePointer);
            queries.add(query);
        }

        if  ((queries != null) && (queries.size() > 0)) {
            ParseQuery<Message> mainQuery = ParseQuery.or(queries);
            mainQuery.orderByAscending("createdAt");
            mainQuery.findInBackground(new FindCallback<Message>() {
                @Override
                public void done(List<Message> objects, ParseException e) {
                    if (e == null) {
                        Log.i("Query_Size", "");
                        System.out.println(objects.size());
                        for (int i = 0; i < objects.size(); i++) {
                            if (objects.get(i).getActive()) {
                                mMessages.add(objects.get(i));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }
}
