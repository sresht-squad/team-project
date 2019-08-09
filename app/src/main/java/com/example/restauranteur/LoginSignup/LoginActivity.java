package com.example.restauranteur.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.Customer.Activity.CustomerHomeActivity;
import com.example.restauranteur.Customer.Activity.CustomerNewVisitActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.R;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import static com.parse.ParseUser.getCurrentUser;
import static com.parse.ParseUser.logInInBackground;

public class LoginActivity extends AppCompatActivity {

    private EditText etGetUsername;
    private EditText etGetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        etGetUsername = findViewById(R.id.etUsernameLogin);
        etGetPassword = findViewById(R.id.etPasswordLogin);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignup);

        //user persisting
        final ParseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getBoolean("server")) {
                final Intent intent = new Intent(LoginActivity.this, ServerHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                final Customer customer = new Customer(currentUser);
                Intent intent;
                //persists current visit
                if (customer.getCurrentVisit() == null) {
                    resetPreferences();
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
                resetPreferences();
                final String username = etGetUsername.getText().toString();
                final String password = etGetPassword.getText().toString();
                login(username, password);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(LoginActivity.this, SignupActivity.class);
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
                    final Intent intent;
                    if (getCurrentUser().getBoolean("server")) {
                        intent = new Intent(LoginActivity.this, ServerHomeActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, CustomerNewVisitActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

   void resetPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("showCheckButton", true); // value to store
        editor.commit();
    }
}
