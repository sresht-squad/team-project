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

import com.example.restauranteur.LoginActivity;
import com.example.restauranteur.Model.Customer;
import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.Model.Installation;
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

    EditText etServerId;
    Button btnNewVisit;
    EditText etTableNumber;
    ImageView ivLogo;
    Visit visit;
    Server server;
    ImageView logout;
    Installation installation;
    ParsePush push;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_visit);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.myToolbar));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etServerId = findViewById(R.id.etServerId);
        btnNewVisit = findViewById(R.id.btnNewVisit);
        etTableNumber = findViewById(R.id.tvTableNumber);
        ivLogo = findViewById(R.id.ivLogo);



        btnNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                final String tableNum = etTableNumber.getText().toString();

                if (serverId.equals("")){
                    Toast.makeText(CustomerNewVisitActivity.this, "Please enter a server Id", LENGTH_LONG).show();
                    Log.i("Server", "Invalid");
                }
                else if (tableNum.equals("")) {
                    Toast.makeText(CustomerNewVisitActivity.this, "Please enter a table number", LENGTH_LONG).show();
                    Log.i("Table", "Invalid");

                } else {

                    //query for the server with the username/serverId that the customer entered
                    final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereEqualTo("username", serverId);

                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() == 0) {
                                    Toast.makeText(CustomerNewVisitActivity.this, "Please enter a valid server ID", LENGTH_LONG).show();
                                    Log.i("Server", "Invalid");
                                    return;
                                }
                                    server = new Server(objects.get(0));

                                    //check if visit already exists & this is another customer at same table
                                    final Visit.Query parseVisitQuery = new Visit.Query();
                                    parseVisitQuery.checkSameVisit(server, tableNum);

                                    parseVisitQuery.findInBackground(new FindCallback<Visit>() {
                                        @Override
                                        public void done(List<Visit> visitObjects, ParseException e) {
                                            if (e == null) {
                                                //if visit doesn't already exist, create new visit
                                                if (visitObjects.size() == 0 || !visitObjects.get(0).getActive()) {
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
                                                            if (e != null) {
                                                                Log.d("Saving", "Error while saving");
                                                                e.printStackTrace();
                                                            } else {
                                                                // Log lets user know the visit was created
                                                                Log.d("Saving Visit Object", "success");

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

                                                                                Log.d("Saving", "success");
                                                                            }
                                                                        });

                                                                    }
                                                                });

                                                                customerInfo.saveInBackground(new SaveCallback() {
                                                                    @Override
                                                                    public void done(ParseException e) {
                                                                        if (e == null) {
                                                                            Log.d("Saved visit in CustInfo", "success");
                                                                        } else {
                                                                            Log.d("Saved visit in CustInfo", "FAILURE");
                                                                        }
                                                                    }
                                                                });
                                                                Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                }
                                                //if visit already exists, add customer to visit
                                                else {
                                                    Visit sameVisit = visitObjects.get(0);
                                                    sameVisit.addCustomer(Customer.getCurrentCustomer());
                                                    sameVisit.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e != null) {
                                                                Log.d("Saving", "Error while saving sameVisit");
                                                                e.printStackTrace();
                                                            } else {
                                                                Log.d("Saving", "success");
                                                            }
                                                            Intent intent = new Intent(CustomerNewVisitActivity.this, CustomerHomeActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                }
                                            } else {
                                                e.printStackTrace();
                                                Log.d("CHECKSAMEVISIT", "objects null");
                                            }
                                        }
                                    });
                            }else {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

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

