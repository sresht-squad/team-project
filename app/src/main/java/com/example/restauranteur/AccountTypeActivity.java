package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

public class AccountTypeActivity extends AppCompatActivity {

    Button btnCustomer;
    Button btnServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);

        //user persisting
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (currentUser instanceof Server){
                Intent intent = new Intent(AccountTypeActivity.this,ServerHomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(AccountTypeActivity.this,CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            }

        }

        btnCustomer = findViewById(R.id.btnCustomer);
        btnServer = findViewById(R.id.btnServer);

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountTypeActivity.this, CustomerLoginSignupActivity.class);
                startActivity(i);
            }
        });


        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountTypeActivity.this, ServerLoginSignupActivity.class);
                startActivity(i);
            }
        });

    }
}
