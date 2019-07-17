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
