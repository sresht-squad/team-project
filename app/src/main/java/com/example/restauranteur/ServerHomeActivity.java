package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class ServerHomeActivity extends AppCompatActivity {

    ImageView logout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        //implementing fragments
        bottomNavigationView =  findViewById(R.id.bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define fragments here
        final Fragment profile = new ServerProfileFragment();
        final Fragment requests = new ServerRequestsFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        fragment = profile;
                        break;
                    case R.id.requests:
                        fragment = requests;
                        break;
                    default:
                        fragment = profile;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
        });
        //set default
        bottomNavigationView.setSelectedItemId(R.id.profile);


        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.logOut();
                Intent intent = new Intent(ServerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);

            }
        });
    }
}
