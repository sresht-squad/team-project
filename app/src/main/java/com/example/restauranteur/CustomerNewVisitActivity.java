package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Server;
import com.example.restauranteur.models.Visit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.restauranteur.models.Customer.getCurrentUser;

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
        etTableNumber = findViewById(R.id.etTableNumber);

        //create a new visit
        btnNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                String tableNum = etTableNumber.getText().toString();
                visit = new Visit();

               // Customer currentCustomer = new Customer(ParseUser.getCurrentUser());
                visit.setCustomer(ParseUser.getCurrentUser());
                visit.setTableNumber(tableNum);

                //query for the server with the serverId that the customer entered
                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("serverId", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            //Server server = new Server(objects.get(0));
                            ParseUser server = objects.get(0);
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
                                        intent.putExtra("VISIT", visit);
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
