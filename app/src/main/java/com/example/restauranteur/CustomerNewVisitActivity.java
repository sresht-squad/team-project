package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Server;
import com.example.restauranteur.models.Visit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class CustomerNewVisitActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnCreateVisit;
    EditText etTableNumber;
    Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_visit);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        etTableNumber = findViewById(R.id.etTableNumber);

        //create a new visit
        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                String tableNum = etTableNumber.getText().toString();
                visit = new Visit();

                //set the customer of this visit to the current customer
                //the table number is 2 for now TODO: set table numbers
                Customer currentCustomer = new Customer(ParseUser.getCurrentUser());
                visit.setCustomer(currentCustomer);
                visit.setTableNumber(tableNum);

                //query for the server with the serverId that the customer entered
                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("serverId", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            int size_of_query_result = Log.d("SIZE OF QUERY RESULT", Integer.toString(objects.size()));
                            Server server = new Server(objects.get(0));
                            //set the server
                            visit.setServer(server);
                            visit.saveInBackground(new SaveCallback() {
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
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
    }

}
