package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CustomerLoginSignupActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button btLogin;
    TextView btSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login_signup);

        usernameInput = findViewById(R.id.etUsernameCustomer);
        passwordInput = findViewById(R.id.etPasswordCustomer);
        btLogin = findViewById(R.id.btLoginCustomer);
        btSignUp = findViewById(R.id.btSignupCustomer);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                signUp(username, password);
            }
        });
    }

    private void signUp(final String username, final String password){
        // Create the ParseUser
        ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.put("server", false);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    finish();
                    login(username, password);
                } else {
                    Log.d("Sign up", "sign up failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void login(final String username, final String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("Login","Login success");
                    final Intent intent = new Intent(CustomerLoginSignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Log.e("Login", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
