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
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
                final String serverId = etServerId.getText().toString();
                final String tableNum = etTableNumber.getText().toString();

                //query for the server with the username/serverId that the customer entered
                final ParseQuery<ParseUser> parseUserQuery = ParseUser.getQuery();
                parseUserQuery.whereEqualTo("username", serverId);

                parseUserQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            final Server server = new Server(objects.get(0));
                            //check if visit already exists & this is another customer at same table
                            final ParseQuery<Visit> parseVisitQuery = new Visit.Query();
                            ((Visit.Query) parseVisitQuery).checkSameVisit(server, tableNum);

                            parseVisitQuery.findInBackground(new FindCallback<Visit>() {
                                @Override
                                public void done(List<Visit> objects, ParseException e) {
                                    //if visit doesn't already exist, create new visit
                                    if (objects != null){
                                        //creating new visit
                                        visit = new Visit();
                                        //add customer to new visit and visit to customer
                                        Customer customer = Customer.getCurrentCustomer();
                                        visit.put("customers", new ArrayList<ParseUser>());
                                        visit.addCustomer(customer);
                                        customer.setVisit(visit);

                                        visit.setTableNumber(tableNum);
                                        visit.setActive(true);
                                        visit.setServer(server);
                                        visit.put("messages", new ArrayList<Message>());

                                        visit.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e != null){
                                                    Log.d("Saving","Error while saving");
                                                    e.printStackTrace();
                                                }else{
                                                    Log.d("Saving", "success");
                                                    Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                    //if visit already exists
                                    else{

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
