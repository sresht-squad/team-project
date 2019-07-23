package com.example.restauranteur.Server.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.example.restauranteur.Model.Server;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class ServerLoginSignupActivity extends AppCompatActivity {


    private EditText etGetUsername;
    private EditText etGetPassword;
    Button btnLogin;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        //connecting to layout
        etGetUsername = findViewById(R.id.etUsername);
        etGetPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etGetUsername.getText().toString();
                String password = etGetPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newServer();
                Toast.makeText(ServerLoginSignupActivity.this, "You are now signed up as a server, click above to login!", LENGTH_LONG)
                        .show();
            }
        });

    }

    void newServer (){
        final Server server =  new Server(new ParseUser());
        // Set core properties
        final String newUsername = etGetUsername.getText().toString();
        final String newPassword = etGetPassword.getText().toString();

        server.setUsername(newUsername);
        server.setPassword(newPassword);
        server.put("server", true);

        //initializing server's visit Array
        ArrayList<Visit> visitsArrayList = new ArrayList<Visit>();
        server.put("visits", visitsArrayList);

        // Invoke signUpInBackground
        server.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null ){
                    Log.i("ServerSignup", "New Server created");
                }else {
                    Log.i("ServerSignup", "New server failed");
                }
            }
        });
    }

    //Server decides to login
    private void login(final String username, final String password){

        Server.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.i("Login","Login success");
                    final Intent intent = new Intent(ServerLoginSignupActivity.this, ServerHomeActivity.class);
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

