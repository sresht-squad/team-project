package com.example.restauranteur;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class SignupActivity extends AppCompatActivity {


    private EditText etGetUsername;
    private EditText etGetPassword;
    private EditText etGetFirstName;
    private EditText etGetLastName;
    RadioGroup serverOrCustomer;
    RadioButton rbServer;
    RadioButton rbCustomer;
    Button btnLogin;
    Button btnSignup;
    Server server;
    ServerInfo serverInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //connecting to layout
        etGetFirstName = findViewById(R.id.etFirstName);
        etGetLastName = findViewById(R.id.etLastName);
        etGetUsername = findViewById(R.id.etUsername);
        etGetPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
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
                    finish();
                } else if (selectedId == rbCustomer.getId()){
                    signUpCustomer(newUsername, newPassword, firstName, lastName);
                    finish();

                }
            }
        });
    }

    void signUpServer(String newUsername, String newPassword, String first, String last) {

        server = new Server(new ParseUser());
        server.setUsername(newUsername);
        server.setPassword(newPassword);
        server.setFirstName(first);
        server.setLastName(last);
        server.put("server", true);

        // Invoke signUpInBackground
        server.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.i("ServerSignup", "New Server created");
                    createServerInfo();
                    server.put("serverInfo",serverInfo);
                    server.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });

                } else {
                    Log.i("ServerSignup", "server signup failed");
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

    private void signUpCustomer(String newUsername, String newPassword, String first, String last){
        Log.d("signup","signup pressed");
        // Create the Customer
        Customer customer = new Customer(new ParseUser());

        // Set core properties
        customer.setUsername(newUsername);
        customer.setPassword(newPassword);
        customer.setFirstName(first);
        customer.setLastName(last);
        customer.put("server", false);
        // Invoke signUpInBackground
        customer.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "You are now signed up as a customer, click above to login!", LENGTH_LONG).show();
                } else {
                    Log.d("Sign up", " Customer sign up failure");
                    e.printStackTrace();
                }
            }
        });
    }
}

