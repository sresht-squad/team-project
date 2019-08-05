package com.example.restauranteur.Customer.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restauranteur.R;
import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

public class CustomerRequestsFragment extends Fragment {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Customer customer;
    private Visit visit;

    private SwipeRefreshLayout swipeContainer;


    public CustomerRequestsFragment() {
        //empty constructor required
    }

    public static CustomerRequestsFragment newInstance(int page, String title) {
        CustomerRequestsFragment fragmentFirst = new CustomerRequestsFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
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
        final ImageButton ibCheck = view.findViewById(R.id.ibCheck);


        ibServerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverHelpRequest = "In-person assistance";
                changeColors(ibServerHelp);
                generateQuickRequest(serverHelpRequest);
            }
        });

        //sending the waiter a request to get the water
        //still need to connect to visit
        ibRefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterRequest = "Need a refill";
                changeColors(ibRefill);
                generateQuickRequest(waterRequest);
            }
        });


        //sending the waiter request to get the check
        //still need to connect to visit
        ibCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkRequest = "Ready for Check";
                changeColors(ibCheck);
                generateQuickRequest(checkRequest);
            }
        });

        //sending the waiter request to get the check
        //still need to connect to visit
        ibToGoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toGoBoxRequest = "To go boxes";
                changeColors(ibToGoBox);
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
        final String userId = Customer.getCurrentCustomer().getObjectId();
        Log.d("current customer", userId);
        mAdapter = new ChatAdapter(getContext(), false, false, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);


        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayCurrentMessages();
                //now make sure swipeContainer.setRefreshing is set to false in displayCurrentMessages()
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        setupMessagePosting();
        displayCurrentMessages();

    }

    private void changeColors(final ImageView view){
        view.setBackgroundResource(R.drawable.rounded_image_button_pressed);
        sleep(150);
        view.setBackgroundResource(R.drawable.rounded_image_button_selector);

    }

    private void generateQuickRequest( final String request){

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
                        Log.i("VisitMessage", "added message to visit");
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Your Orders");
    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().length() > 0){
                    postMessage();
                }
            }
        });
    }


    private void postMessage() {
        String data = etMessage.getText().toString();
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
        Log.i("DISPLAY", "ALL_MESSAGES");
        if (mMessages != null) {
            mMessages.clear();
        }
        List<ParseObject> messageList = visit.getMessageList();
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
}