package com.example.restauranteur;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.Customer.Activity.CustomerNewVisitActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.Model.Installation;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.Server.Activity.ServerHomeActivity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.parse.ParseUser.getCurrentUser;
import static com.parse.ParseUser.logInInBackground;

public class SignupActivity extends AppCompatActivity {


    private EditText etGetUsername;
    private EditText etGetPassword;
    private EditText etGetFirstName;
    private EditText etGetLastName;
    RadioGroup serverOrCustomer;
    RadioButton rbServer;
    RadioButton rbCustomer;
    Button btnSignup;
    Server server;
    ServerInfo serverInfo;
    CustomerInfo customerInfo;
    ParseInstallation installation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //connecting to layout
        etGetFirstName = findViewById(R.id.etFirstName);
        etGetLastName = findViewById(R.id.etLastName);
        etGetUsername = findViewById(R.id.etUsername);
        etGetPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup2);
        serverOrCustomer = findViewById(R.id.radioGroup);
        rbServer = findViewById(R.id.rbServer);
        rbCustomer = findViewById(R.id.rbCustomer);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newUsername = etGetUsername.getText().toString();
                final String newPassword = etGetPassword.getText().toString();
                final String firstName = etGetFirstName.getText().toString();
                final String lastName = etGetLastName.getText().toString();
                int selectedId = serverOrCustomer.getCheckedRadioButtonId();

                if (selectedId == rbServer.getId()) {
                    signUpServer(newUsername, newPassword, firstName, lastName);

                } else if (selectedId == rbCustomer.getId()){
                    signUpCustomer(newUsername, newPassword, firstName, lastName);

                }
            }
        });
    }

    void signUpServer(final String newUsername, final String newPassword, String first, String last) {

        server = new Server(new ParseUser());
        server.setUsername(newUsername);
        server.setPassword(newPassword);
        server.setFirstName(first);
        server.setLastName(last);
        server.put("server", true);

        //check if username is already taken
        //query for whether there is a user with the username that this user entered
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", newUsername);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                //if there is another user with this username, toast
                if (objects.size() > 0){
                    Toast toast = Toast.makeText(SignupActivity.this, "Username is already taken", LENGTH_LONG);
                    View view = toast.getView();
                    //Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    //Gets the TextView from the Toast so it can be edited
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    toast.show();
                }
                //otherwise, sign up the user
                else{
                    // Invoke signUpInBackground
                    server.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Log.i("ServerSignup", "New Server created");
                                // create the serverInfo object
                                createServerInfo();

                                // connect the new created serverInfo with the new server
                                server.put("serverInfo",serverInfo);
                                server.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        //create the installation for that user
                                        createInstallation();
                                        server.put("installation", installation);
                                        server.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                login(newUsername,newPassword);
                                            }
                                        });
                                    }
                                });

                            } else {
                                Log.i("ServerSignup", "server signup failed");
                            }
                        }
                    });
                }
            }
        });
    }

    private void createInstallation (){

       installation = new Installation();
       installation.put("channels", new ArrayList<String>());
       installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Installation", "New Installation");

                } else {
                    Log.i("Installation", "installation failed");
                }
            }
        });

    }


    private void createServerInfo (){
        // call this in callBack to gurantee association
        // put into the method and doing it the save callBack
        serverInfo = new ServerInfo();
        serverInfo.put("visits",new ArrayList<Visit>());
        //invoke saveInBackground
        serverInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("ServerInfo", "New ServerInfo");

                } else {
                    Log.i("ServerInfo", "ServerInfo not working");
                }
            }
        });
    }

    private void createCustomerInfo (){
        // call this in callBack to gurantee association
        // put into the method and doing it the save callBack
        customerInfo = new CustomerInfo();
        //invoke saveInBackground
        customerInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("CustomerInfo", "New CustomerInfo");

                } else {
                    Log.i("CustomerInfo", "Customer Info not working");
                }
            }
        });
    }

    private void signUpCustomer(final String newUsername, final String newPassword, String first, String last){
        Log.d("signup","signup pressed");
        // Create the Customer
        final Customer customer = new Customer(new ParseUser());

        // Set core properties
        customer.setUsername(newUsername);
        customer.setPassword(newPassword);
        customer.setFirstName(first);
        customer.setLastName(last);
        customer.put("server", false);

        //check if username is already taken
        //query for whether there is a user with the username that this user entered
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", newUsername);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                //if there is another user with this username, toast
                if (objects.size() > 0){
                    Toast toast = Toast.makeText(SignupActivity.this, "Username is already taken", LENGTH_LONG);
                    View view = toast.getView();
                    //Gets the actual oval background of the Toast then sets the colour filter
                    view.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                    //Gets the TextView from the Toast so it can be edited
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    toast.show();
                }
                //otherwise, sign up the user
                else{
                    // Invoke signUpInBackground
                    customer.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                createCustomerInfo();
                                customer.put("customerInfo",customerInfo);
                                customer.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        login(newUsername, newPassword);
                                    }
                                });
                            } else {
                                Log.d("Sign up", " Customer sign up failure");
                                e.printStackTrace();
                            }
                        }
                    });
                }
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
                        intent = new Intent(SignupActivity.this, ServerHomeActivity.class);
                    } else {
                        intent = new Intent(SignupActivity.this, CustomerNewVisitActivity.class);
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

