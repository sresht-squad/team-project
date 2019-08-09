package com.example.restauranteur.Customer.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.RequestsAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CustomerRequestsFragment extends Fragment {

    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private RequestsAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Customer customer;
    private ImageButton ibCheck;
    private SwipeRefreshLayout swipeContainer;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Visit visit;


    public CustomerRequestsFragment() {
        setRetainInstance(true);
    }

    public static CustomerRequestsFragment newInstance(int page, String title) {
        CustomerRequestsFragment fragmentFirst = new CustomerRequestsFragment();
        Bundle args = new Bundle();
        args.putInt("int", page);
        args.putString("title", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_customer_requests, parent, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mMessages = new ArrayList<>();
        ibCheck = view.findViewById(R.id.ibCheck);
        mAdapter = new RequestsAdapter(getContext(), false, false, mMessages);
        customer = Customer.getCurrentCustomer();
        visit = customer.getCurrentVisit();
        refreshView(view);
    }


    private void generateQuickRequest(final String request) {
        final Message message = new Message();
        message.setAuthor(customer);
        message.setBody(request);
        message.setActive(true);

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                visit.addMessage(message);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mMessages.add(message);
                        mAdapter.notifyItemInserted(mMessages.size() - 1);
                        rvChat.scrollToPosition(mMessages.size() - 1);
                    }
                });
            }
        });
    }


    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().length() > 0) {
                    postMessage();
                }
            }
        });
    }


    private void postMessage() {
        final String data = etMessage.getText().toString();
        // Using new `Message` Parse-backed model now
        final Message message = new Message();
        message.setBody(data);
        message.setAuthor(customer);
        message.setActive(true);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //if the message saves successfully, save it to the visit as well
                visit.addMessage(message);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        displayNewMessage(message);
                        etMessage.setText(null);
                    }
                });
            }
        });
    }

    private void displayNewMessage(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged(); // update adapter
    }


    private void displayCurrentMessages() {
        if (mMessages != null) {
            mMessages.clear();
        }
        final List<ParseObject> messageList = visit.getMessageList();
        ParseObject.fetchAllInBackground(messageList);
        for (int i = 0; i < messageList.size(); i++) {
            Message message = (Message) messageList.get(i);
            if (message.getActive()) {
                mMessages.add(message);
                mAdapter.notifyDataSetChanged();
            }
        }
        swipeContainer.setRefreshing(false);
    }

    private void checkForCompletedOrders() {
        //delete orders that server has marked as complete
        for (int i = 0; i < mMessages.size(); i++) {
            Message message = mMessages.get(i);
            if (!message.getActive()) {
                mMessages.remove(message);
                mAdapter.notifyDataSetChanged();
            }
        }
        swipeContainer.setRefreshing(false);
    }

    private void refreshView(View view){

        final ImageButton ibServerHelp = view.findViewById(R.id.ibServerHelp);
        final ImageButton ibRefill = view.findViewById(R.id.ibRefill);
        final ImageButton ibToGoBox = view.findViewById(R.id.ibToGoBox);

        //sending the waiter a request for general help

        ibServerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverHelpRequest = "In-person assistance";
                generateQuickRequest(serverHelpRequest);
            }
        });

        //sending the waiter a request to get the water
        ibRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterRequest = "Refill";
                generateQuickRequest(waterRequest);
            }
        });


        //sending the waiter request to get the check
        //still need to connect to visit

        if (preferences.getBoolean("showCheckButton",true)) {
            ibCheck.setVisibility(View.VISIBLE);
            ibCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String checkRequest = "Check";
                    generateQuickRequest(checkRequest);
                    ibCheck.setVisibility(View.GONE);
                    //saves status even if app is destroyed
                    editor.putBoolean("showCheckButton", false);
                    // Commit the edits!
                    editor.commit();
                }
            });
        } else {
            ibCheck.setVisibility(View.GONE);
        }

        //sending the waiter request to get the check
        ibToGoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toGoBoxRequest = "To go boxes";
                generateQuickRequest(toGoBoxRequest);
            }
        });

        // Find the text field and button
        etMessage = view.findViewById(R.id.etMessage);
        btSend = view.findViewById(R.id.btSend);
        rvChat = view.findViewById(R.id.rvChat);


        //find and set the adapter
        mAdapter = new RequestsAdapter(getContext(), false, false, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);


        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkForCompletedOrders();
                //ensure swipeContainer.setRefreshing is set to false in displayCurrentMessages()
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.lightBlueMaterialDesign,
                R.color.yellow,
                android.R.color.holo_red_light);

        setupMessagePosting();
        displayCurrentMessages();
    }

    @Override
    public void onResume() {
        refreshView(getView());
        super.onResume();
    }
}