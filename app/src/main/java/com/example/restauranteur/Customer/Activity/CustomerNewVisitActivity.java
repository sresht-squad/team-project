package com.example.restauranteur.Customer.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.LoginSignup.LoginActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.Server;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.example.restauranteur.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class CustomerNewVisitActivity extends AppCompatActivity {

    private EditText etServerId;
    private Button btnNewVisit;
    private EditText etTableNumber;
    private Visit visit;
    private Server server;
    private String tableNum;
    private String serverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_visit);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etServerId = findViewById(R.id.etServerId);
        btnNewVisit = findViewById(R.id.btnNewVisit);
        etTableNumber = findViewById(R.id.tvTableNumber);


        btnNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverId = etServerId.getText().toString();
                tableNum = etTableNumber.getText().toString();

                if (serverId.equals("")){
                    Toast.makeText(CustomerNewVisitActivity.this, "Please ask your server for their ID", LENGTH_LONG).show();
                }
                else if (tableNum.equals("")) {
                    Toast.makeText(CustomerNewVisitActivity.this, "Please ask your server for the table number ", LENGTH_LONG).show();

                }
                else {
                    findServer();
                }
            }
        });
    }

    private void findServer(){
        //query for the server with the username/serverId that the customer entered
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", serverId);

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        Toast.makeText(CustomerNewVisitActivity.this, "Invalid server ID. Please ask your server for their ID", LENGTH_LONG).show();
                        return;
                    }
                    server = new Server(objects.get(0));
                    checkVisitExists();

                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    //check if visit already exists & this is another customer at same table
    private void checkVisitExists(){
        final Visit.Query parseVisitQuery = new Visit.Query();
        parseVisitQuery.checkSameVisit(server, tableNum);

        parseVisitQuery.findInBackground(new FindCallback<Visit>() {
            @Override
            public void done(List<Visit> visitObjects, ParseException e) {
                if (e == null) {
                    //if visit doesn't already exist, create new visit
                    if (visitObjects.size() == 0 || !visitObjects.get(0).getActive()) {
                        createNewVisit();
                    }
                    //if visit already exists, add customer to visit
                    else {
                        final Visit sameVisit = visitObjects.get(0);
                        addCustomerToVisit(sameVisit);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addCustomerToVisit(Visit sameVisit){
        sameVisit.addCustomer(Customer.getCurrentCustomer());
        sameVisit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                final Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createNewVisit(){
        //creating new visit
        visit = new Visit();
        Customer customer = Customer.getCurrentCustomer();
        //add customer to new visit
        visit.put("customers", new ArrayList<ParseUser>());
        visit.addCustomer(customer);
        visit.setTableNumber(tableNum);
        visit.setActive(true);
        final CustomerInfo customerInfo = customer.getInfo();
        customerInfo.setVisit(visit);
        ArrayList<Message> messageArrayList = new ArrayList<Message>();
        visit.put("messages", messageArrayList);
        visit.setServer(server);
        visit.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    addToServerInfo();

                    //add to customerInfo
                    customerInfo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    });

                    final Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void addToServerInfo(){
        //Adds the current new Visit into the serverInfo's visit's array
        //Query to find the serverInfo object that matches the server
        ParseQuery<ServerInfo> query = ParseQuery.getQuery(ServerInfo.class);
        query.whereEqualTo("objectId", server.getServerInfo().getObjectId());

        query.findInBackground(new FindCallback<ServerInfo>() {
            @Override
            public void done(List<ServerInfo> objects, ParseException e) {
                // ServerInfo is made into the ServerInfo Object found
                ServerInfo serverInfo = objects.get(0);
                // inside the serverInfo object we add a visit to the Visit array
                serverInfo.addVisit(visit);
                serverInfo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(false);
    }



    public void setTitle(String title) {
        getActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customer_menu_top, menu);

        MenuItem miLogout = menu.findItem(R.id.ivLogout);
        miLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Customer.logOut();
                Intent intent = new Intent(CustomerNewVisitActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onResume(){
        super.onResume();
        final Intent intent = getIntent();
        //check if the message actually came from NFC
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //get the data
            final Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            String serverId = "";
            String tableNum = "";
            NdefMessage ndefmessage = null; // only one message transferred
            if (rawMessages != null) {
                ndefmessage = (NdefMessage) rawMessages[0];
                //get each record and convert them back into string format
                serverId = new String(ndefmessage.getRecords()[0].getPayload());
                tableNum = new String(ndefmessage.getRecords()[1].getPayload());
            }
            //set the editText to contain the first message
            etServerId.setText(serverId);
            etTableNumber.setText(tableNum);
            if (!etTableNumber.getText().equals("")){
                btnNewVisit.callOnClick();
            }

        }
    }

}

