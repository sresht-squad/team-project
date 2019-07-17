package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etWaiterId;
    Button btnCreateVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etWaiterId = findViewById(R.id.etWaiterId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);


    }
}
