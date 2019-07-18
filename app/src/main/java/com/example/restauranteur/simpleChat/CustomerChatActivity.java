package com.example.restauranteur.simpleChat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.restauranteur.R;

public class CustomerChatActivity extends ServerChatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat);
        startWithCurrentUser();
        refreshMessages();
    }
}
