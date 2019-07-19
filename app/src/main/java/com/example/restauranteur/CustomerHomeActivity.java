package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.restauranteur.fragment.CustomerChatFragment;
import com.example.restauranteur.fragment.CustomerQuickRequestFragment;
import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Visit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class CustomerHomeActivity extends AppCompatActivity {
    ImageView logout;
    BottomNavigationView customerBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        customerBottomNavigation = findViewById(R.id.bottom_navigation);


        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new CustomerQuickRequestFragment();

                switch (menuItem.getItemId()) {
                    case R.id.action_request:
                        fragment = new CustomerQuickRequestFragment();
                        Toast.makeText(CustomerHomeActivity.this, "fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_visit:
                        fragment = new CustomerChatFragment();
                        Toast.makeText(CustomerHomeActivity.this, "fragment", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
        });

        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer.getCurrentUser().logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });

    }
}
