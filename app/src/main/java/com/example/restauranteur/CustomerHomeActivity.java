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

    EditText etServerId;
    Button btnCreateVisit;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        logout = findViewById(R.id.ivLogout);

        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                ParseUser currentCustomer = ParseUser.getCurrentUser();

                if (serverId.equals(currentCustomer.getString("serverId"))){
                    Visit visit = new Visit();
                    visit.setCustomer(currentCustomer);
//                    visit.setServer();
                }
            }
        });


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
