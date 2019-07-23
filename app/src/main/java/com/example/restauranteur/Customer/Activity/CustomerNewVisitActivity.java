package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.AccountTypeActivity;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.R;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerNewVisitActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnNewVisit;
    EditText etTableNumber;
    Visit visit;
    ImageView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_visit);

        etServerId = findViewById(R.id.etServerId);
        btnNewVisit = findViewById(R.id.btnNewVisit);
        etTableNumber = findViewById(R.id.tvTableNumber);

        //create a new visit
        btnNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                String tableNum = etTableNumber.getText().toString();
                visit = new Visit();
                Customer customer = Customer.getCurrentCustomer();
                visit.put("customers", new ArrayList<ParseUser>());
                visit.addCustomer(customer);
                visit.setTableNumber(tableNum);
                visit.setActive(true);
                customer.setVisit(visit);
                ArrayList<Message> messageArraylist = new ArrayList<Message>();
                visit.put("messages", messageArraylist);

                //query for the server with the username/serverId that the customer entered
                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            //Server server = new Server(objects.get(0));
                            Server server = new Server(objects.get(0));
                            visit.setServer(server);
                            visit.setActive(true);
                            ParseACL acl = new ParseACL();
                            acl.setReadAccess(ParseUser.getCurrentUser(),true);
                            acl.setWriteAccess(ParseUser.getCurrentUser(),true);
                            visit.setACL(acl);
                            visit.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.d("Saving", "Error while saving");
                                        e.printStackTrace();
                                    } else {
                                        Log.d("Saving", "success");
                                        Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer.logOut();
                Intent intent = new Intent(CustomerNewVisitActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });
    }
}
