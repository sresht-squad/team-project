package com.example.restauranteur.Customer.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.restauranteur.RequestsAdapter;
import com.example.restauranteur.R;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
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
    private Visit visit;
    private ImageButton ibCheck;
    private boolean sentCheck;
    private SwipeRefreshLayout swipeContainer;


    public CustomerRequestsFragment() {
        //empty constructor required
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_customer_requests, parent, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ImageButton ibServerHelp = view.findViewById(R.id.ibServerHelp);
        final ImageButton ibRefill = view.findViewById(R.id.ibRefill);
        final ImageButton ibToGoBox = view.findViewById(R.id.ibToGoBox);
        ibCheck = view.findViewById(R.id.ibCheck);

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
        //if the check has already been requested, it will go away
        if (sentCheck) {
            ibCheck.setVisibility(View.GONE);
        } else {
            ibCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String checkRequest = "Check";
                    generateQuickRequest(checkRequest);
                    ibCheck.setVisibility(View.GONE);
                    sentCheck = true;
                }
            });
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
        mMessages = new ArrayList<>();
        customer = new Customer(ParseUser.getCurrentUser());
        visit = customer.getCurrentVisit();

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
                        mMessages.clear();
                        displayCurrentMessages();
                        rvChat.scrollToPosition(mMessages.size() - 1);
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
}