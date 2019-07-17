package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnCreateVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);


    }
}
