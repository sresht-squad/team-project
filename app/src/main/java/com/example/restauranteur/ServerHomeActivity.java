package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Random;

public class ServerHomeActivity extends AppCompatActivity {

    TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        //matching server to customer

        tvId = findViewById(R.id.tvId);

//        ParseUser.getCurrentUser().fetchInBackground();

        ParseUser currentUser = ParseUser.getCurrentUser();
        tvId.setText(currentUser.getString("serverID"));

    }



}
