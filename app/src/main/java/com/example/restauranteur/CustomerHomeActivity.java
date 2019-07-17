package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseUser;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etWaiterId;
    Button btnCreateVisit;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etWaiterId = findViewById(R.id.etWaiterId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);


        logout = findViewById(R.id.ivLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });


    }
}
