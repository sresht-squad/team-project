package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.R;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;
import java.util.Random;

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
            }
        });

    }

    //Server decides to login
    private void login(final String username, final String password){

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.i("Login","Login success");
                    final Intent intent = new Intent(ServerLoginSignupActivity.this,ServerHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Log.e("Login", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
    //being able to identify between the customer and server
    /*ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
    // Create the ParseUser
        userQuery.whereEqualTo("server", "True");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
        public void done(List<ParseUser> results, ParseException e) {
            // results has the list of users who are admins
        }
    });*/

    //query.include(Post.KEY_USER)
    void newServer (){
        final ParseUser user =  new ParseUser();
        // Set core properties
        final String newUsername = etGetUsername.getText().toString();
        final String newPassword = etGetPassword.getText().toString();

        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.put("server", true);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null ){
                    Log.i("ServerSignup", "New Server created");
                    ServerID serverID = new ServerID();

                    serverID.setServer(user);
                    serverID.setIdNumber(getRandomAlphaNum(10));

                    serverID.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null){
                                Log.d("Saving","Error while saving");
                                e.printStackTrace();
                                return;
                            }else{
                                Log.d("Saving", "success");
                            }
                        }
                    });
                    login(newUsername, newPassword);
                }else {
                    Log.i("ServerSignup", "New server failed");
                }
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

