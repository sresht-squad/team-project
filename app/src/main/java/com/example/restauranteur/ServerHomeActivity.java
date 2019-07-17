package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Random;

public class ServerHomeActivity extends AppCompatActivity {

    TextView tvId;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        //matching server to customer

        tvId = findViewById(R.id.tvId);
        tvId.setText(getRandomAlphaNum(10));

        logout = findViewById(R.id.ivLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(ServerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });

    }

    protected String getRandomAlphaNum(int len) {
        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randString = new StringBuilder();
        Random rnd = new Random();
        while (randString.length() < len) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            randString.append(CHARS.charAt(index));
        }
        String finalStr = randString.toString();
        return finalStr;

    }

}
