package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.LoginActivity;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.R;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.Visit;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

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

        btnNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                final String tableNum = etTableNumber.getText().toString();

                //query for the server with the username/serverId that the customer entered
                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            final Server server = new Server(objects.get(0));

                            //check if visit already exists & this is another customer at same table
                            final Visit.Query parseVisitQuery = new Visit.Query();
                            parseVisitQuery.checkSameVisit(server, tableNum);

                            parseVisitQuery.findInBackground(new FindCallback<Visit>() {
                                @Override
                                public void done(List<Visit> visitObjects, ParseException e) {
                                    if (e == null){
                                        //if visit doesn't already exist, create new visit
                                        if (visitObjects.size() == 0){
                                            Log.d("CHECKSAMEVISIT", visitObjects.toString());
                                            //creating new visit
                                            visit = new Visit();
                                            Customer customer = Customer.getCurrentCustomer();
                                            //add customer to new visit
                                            visit.put("customers", new ArrayList<ParseUser>());
                                            visit.addCustomer(customer);
                                            visit.setTableNumber(tableNum);
                                            visit.setActive(true);
                                            customer.setVisit(visit);
                                            ArrayList<Message> messageArrayList = new ArrayList<Message>();
                                            visit.put("messages", messageArrayList);
                                            visit.setServer(server);
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
                                        //if visit already exists, add customer to visit
                                        else{
                                            Visit sameVisit = (Visit) visitObjects.get(0);
                                            sameVisit.addCustomer(Customer.getCurrentCustomer());
                                            Customer.getCurrentCustomer().setVisit(sameVisit);
                                            sameVisit.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e != null){
                                                        Log.d("Saving","Error while saving sameVisit");
                                                        e.printStackTrace();
                                                    }else{
                                                        Log.d("Saving", "success");
                                                        }
                                                        Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                        }
                                    }
                                    else{
                                        e.printStackTrace();
                                        Log.d("CHECKSAMEVISIT", "objects null");
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
                Intent intent = new Intent(CustomerNewVisitActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
