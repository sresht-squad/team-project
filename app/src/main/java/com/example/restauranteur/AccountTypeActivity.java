package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Server;
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
            if (currentUser.getBoolean("server")){
                Intent intent = new Intent(AccountTypeActivity.this,ServerHomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Customer c = new Customer(currentUser);
                Intent intent;
                if (c.getCurrentVisit() == null) {
                    intent = new Intent(AccountTypeActivity.this, CustomerNewVisitActivity.class);
                }else {
                    intent = new Intent(AccountTypeActivity.this, CustomerHomeActivity.class);
                }
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
