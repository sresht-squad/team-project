package com.example.restauranteur.Customer.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteur.Model.Server;
import com.example.restauranteur.R;
import com.example.restauranteur.ChatAdapter;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Model.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerRequestsFragment extends Fragment {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private RecyclerView rvChat;
    private  ArrayList<Message> mMessages;
    private ChatAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Customer customer;
    private boolean mFirstLoad;

    public CustomerRequestsFragment() {
        //empty constructor required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_customer_chat, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Find the text field and button
        etMessage = view.findViewById(R.id.etMessage);
        btSend = view.findViewById(R.id.btSend);
        rvChat = view.findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        customer = new Customer(ParseUser.getCurrentUser());
        final String userId = Customer.getCurrentCustomer().getObjectId();
        Log.d("current customer", userId);
        mAdapter = new ChatAdapter(getContext(), userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);

        setupMessagePosting();
        displayCurrentMessages();

    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // associate the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);
        final Customer customer = Customer.getCurrentCustomer();
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
    }


    private void postMessage(){
        final Visit visit = customer.getCurrentVisit();
        String data = etMessage.getText().toString();
        // Using new `Message` Parse-backed model now
        final Message message = new Message();
        message.setBody(data);
        message.setAuthor(customer);
        message.setActive(true);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Created message on Parse", Toast.LENGTH_SHORT).show();
                visit.addMessage(message);
            }
        });
        visit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                displayNewMessage(message);
                etMessage.setText(null);
            }
        });
    }

    private void displayNewMessage(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged(); // update adapter
    }



    private void displayCurrentMessages() {
        Log.i("DISPLAY", "ALL_MESSAGES");
        Visit visit = customer.getCurrentVisit();
        JSONArray messages = visit.getMessages();
        if (mMessages != null) {
            mMessages.clear();
        }
        int length = 0;
        if (messages != null) {
            length = messages.length();
        }
        //only show active messages created by this user during this visit

        String messagePointer = "";
        //for all messages in the visit array
        for (int i = 0; i < length; i++) {
            //get pointer to message from JSON data
            try {
                messagePointer = messages.getJSONObject(i).getString("objectId");
                Log.i("MESSAGE_ID", messagePointer);
            } catch (JSONException e) {
                Log.i("MESS_ERROR", messagePointer);
            }

            final ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
           // query.orderByAscending("createdAt");
            query.whereEqualTo("objectId", messagePointer);

            query.findInBackground(new FindCallback<Message>() {
                @Override
                public void done(List<Message> objects, ParseException e) {
                    if (e == null) {
                        //since we are querying for 1 object id there will only be 1 object
                        mMessages.addAll(objects);
                    }
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    } else {
                        Log.e("message", "Error Loading Messages");
                    }
                }
            });
        }
    }
}
