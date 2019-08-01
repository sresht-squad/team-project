package com.example.restauranteur.Server.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Fragment.ServerActiveVisitsFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ServerVisitDetailActivity extends AppCompatActivity {
    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;
    private Visit visit;
    private FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_visit_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        final Intent intent = getIntent();
        visit = intent.getParcelableExtra("VISIT");
        String nameText = intent.getStringExtra("NAME_TEXT");

        //id lookups
        final RecyclerView rvChat = (RecyclerView) findViewById(R.id.rvChat);
        final TextView tvTableNumber = (TextView) findViewById(R.id.tvTableNumber);
        final Button btnComplete = (Button) findViewById(R.id.btnComplete);

        //get table number
        final String tableNumber = visit.getTableNumber();

        //set table number title text
        tvTableNumber.setText(nameText + ", Table " + tableNumber);

        //set chat adapter
        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, true, true, mMessages);
        rvChat.setAdapter(mAdapter);


        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change boolean true to false.Visit is not Active
                visit.setActive(false);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i("saved", "visit Active false");

                        Fragment fragment = new ServerActiveVisitsFragment();
                        ((ServerActiveVisitsFragment) fragment).fetchActiveVisits();

                        final Intent intent = new Intent(ServerVisitDetailActivity.this, ServerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

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
