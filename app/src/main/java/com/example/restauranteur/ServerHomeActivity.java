package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.simpleChat.ServerChatActivity;
import com.parse.ParseUser;

public class ServerHomeActivity extends AppCompatActivity {

    TextView tvId;
    ImageView logout;
    Button viewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        //matching server to customer

        tvId = findViewById(R.id.tvId);

        logout = findViewById(R.id.ivLogout);

        viewOrders = findViewById(R.id.btViewOrders);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(ServerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);

            }
        });

        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(ServerHomeActivity.this, ServerChatActivity.class);
                startActivity(s);
            }
        });


        ParseUser currentUser = ParseUser.getCurrentUser();
        String serverId = currentUser.getString("serverId");
        tvId.setText(serverId);
    }
}
