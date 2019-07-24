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
import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.Model.Server;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        final Server server = new Server(new ParseUser());
        server.setUsername(newUsername);
        server.setPassword(newPassword);
        server.setFirstName(first);
        server.setLastName(last);
        server.put("server", true);

       /* final ServerInfo serverInfo = new ServerInfo();
        serverInfo.put("Visits",new ArrayList<Visit>());

        serverInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("ServerInfo", "New ServerInfo");

                } else {
                    Log.i("ServerInfo", "ServerInfo not working");
                }

            }
        });*/

        // Invoke signUpInBackground
        server.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.i("ServerSignup", "New Server created");
                    //server.put("serverInfo",serverInfo);

                } else {
                    Log.i("ServerSignup", "server signup failed");
                }
            }
        });
    }

    private void signUpCustomer(String newUsername, String newPassword, String first, String last){
        Log.d("signup","signup pressed");
        // Create the Customer
        Customer customer = new Customer(new ParseUser());
        //CustomerInfo info = new CustomerInfo();

        // Set core properties
        customer.setUsername(newUsername);
        customer.setPassword(newPassword);
        customer.setFirstName(first);
        customer.setLastName(last);
        customer.put("server", false);
        customer.put("customerInfo", new CustomerInfo());
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

