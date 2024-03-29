package com.example.restauranteur.Server.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.RequestsAdapter;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
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
    private RequestsAdapter mAdapter;
    private Visit visit;
    private RecyclerView rvChat;
    private ConstraintLayout clHeadings;
    private TextView tvNoRequests;
    private TextView tvWYLT;
    private Button btnComplete;
    private ImageView ivNoRequests;


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
        final TextView tvTableNumber = findViewById(R.id.tvTableNumber);
        final ImageButton ibBack = findViewById(R.id.ibBack);
        final TextView tvBack = findViewById(R.id.tvBack);

        rvChat = findViewById(R.id.rvChat);
        clHeadings =  findViewById(R.id.clHeadings);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        tvWYLT = findViewById(R.id.tvWYLT);
        btnComplete = findViewById(R.id.btnComplete);
        ivNoRequests = findViewById(R.id.ivNoRequests);

        //get table number
        final String tableNumber = visit.getTableNumber();

        //set table number title text
        String finalText = nameText + ", Table " + tableNumber;
        tvTableNumber.setText(finalText);

        //set chat adapter
        mMessages = new ArrayList<>();
        mAdapter = new RequestsAdapter(this, true, true, mMessages);
        rvChat.setAdapter(mAdapter);


        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change boolean true to false.Visit is not Active
                visit.setActive(false);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        ServerInfo serverInfo = Server.getCurrentServer().getServerInfo();
                        serverInfo.removeVisit(visit);
                        serverInfo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null){
                                    e.printStackTrace();
                                }
                            }
                        });

                        final Intent intent = new Intent(ServerVisitDetailActivity.this, ServerHomeActivity.class);
                        intent.putExtra("DETAIL", true);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        View.OnClickListener back = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        ibBack.setOnClickListener(back);
        tvBack.setOnClickListener(back);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(linearLayoutManager);

        if (visit.getActive()) {
            displayCurrentMessages();
        } else {
            setVisibilities();
        }
    }

    private void displayCurrentMessages() {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            query.whereEqualTo("objectId", messagePointer).whereEqualTo("active", true);
            queries.add(query);
        }

        if(queries.size() > 0) {
            ParseQuery<Message> mainQuery = ParseQuery.or(queries);
            mainQuery.orderByAscending("createdAt");
            mainQuery.findInBackground(new FindCallback<Message>() {
                @Override
                public void done(List<Message> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() == 0){
                            setVisibilities();
                        }
                        else{
                            for (int i = 0; i < objects.size(); i++) {
                                mMessages.add(objects.get(i));
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
        else{
            setVisibilities();
        }
    }

    public void setVisibilities(){
        rvChat.setVisibility(View.GONE);
        clHeadings.setVisibility(View.GONE);
        tvNoRequests.setVisibility(View.VISIBLE);
        tvWYLT.setVisibility(View.VISIBLE);
        btnComplete.setVisibility(View.VISIBLE);
        ivNoRequests.setVisibility(View.VISIBLE);
    }

}
