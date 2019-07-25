package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
import com.example.restauranteur.Customer.Activity.CustomerNewVisitActivity;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.example.restauranteur.Model.Customer;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import static com.parse.ParseUser.getCurrentUser;
import static com.parse.ParseUser.logInInBackground;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnSignUp;
    private EditText etGetUsername;
    private EditText etGetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etGetUsername = findViewById(R.id.etUsernameLogin);
        etGetPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignup);

        //user persisting
        ParseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getBoolean("server")) {
                Intent intent = new Intent(LoginActivity.this, ServerHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Customer customer = new Customer(currentUser);
                Intent intent;
                //persists current visit
                if (customer.getCurrentVisit() == null) {
                    intent = new Intent(LoginActivity.this, CustomerNewVisitActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, CustomerHomeActivity.class);
                }
                startActivity(intent);
            }

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etGetUsername.getText().toString();
                String password = etGetPassword.getText().toString();
                login(username, password);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void login(final String username, final String password) {
        logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("Login", "Login success");
                    final Intent intent;
                    if (getCurrentUser().getBoolean("server")) {
                        intent = new Intent(LoginActivity.this, ServerHomeActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, CustomerNewVisitActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Login", "Login failure");
                }
            }
        });
    }
}
