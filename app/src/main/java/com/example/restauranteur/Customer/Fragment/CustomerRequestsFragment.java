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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        customer = new Customer(ParseUser.getCurrentUser());
        visit = customer.getCurrentVisit();
        final String userId = Customer.getCurrentCustomer().getObjectId();
        Log.d("current customer", userId);
        mAdapter = new ChatAdapter(getContext(), false, false, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(linearLayoutManager);

        setupMessagePosting();
        displayCurrentMessages();

    }

    // Setup button event handler which posts the entered message to Parse
    private void setupMessagePosting() {
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
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
                Toast.makeText(getContext(), "Created message on Parse", Toast.LENGTH_SHORT).show();
                visit.addMessage(message);
                visit.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(getContext(), "Message added to visit", Toast.LENGTH_SHORT).show();
                        displayNewMessage(message);
                        etMessage.setText(null);
                    }
                });
            }
        });
    }

    private void displayNewMessage(Message message) {
        Toast.makeText(getContext(), "Displaying new message", Toast.LENGTH_SHORT).show();
        mMessages.add(message);
        mAdapter.notifyDataSetChanged(); // update adapter
    }


    private void displayCurrentMessages() {
        Log.i("DISPLAY", "ALL_MESSAGES");
        if (mMessages != null) {
            mMessages.clear();
        }
        List<ParseObject> messageList = visit.getMessageList();
        ParseObject.fetchAllIfNeededInBackground(messageList);
        for (int i = 0; i < messageList.size(); i++) {
            Message message = (Message) messageList.get(i);
            if (message.getActive()) {
                mMessages.add(message);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}