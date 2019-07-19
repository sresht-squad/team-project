package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteur.simpleChat.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnCreateVisit;
    ImageView logout;
    Button btnTestCheck;
    EditText etTableNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        logout = findViewById(R.id.ivLogout);
        btnTestCheck = findViewById(R.id.btnTestCheck);
        etTableNumber = findViewById(R.id.etTableNumber);


        //create a new visit
        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                String tableNum = etTableNumber.getText().toString();
                final Visit visit = new Visit();

                //set the customer of this visit to the current customer
                //the table number is 2 for now TODO: set table numbers
                ParseUser currentCustomer = ParseUser.getCurrentUser();
                visit.setCustomer(currentCustomer);
                visit.setTableNumber(tableNum);

                //query for the server with the serverId that the customer entered
                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("serverId", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            Log.d("SIZE OF QUERY RESULT", Integer.toString(objects.size()));
                            ParseUser server = objects.get(0);
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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });

        btnTestCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkMessage = "Ready for Check";

                Message readyForCheck = new Message();
                readyForCheck.setAuthor(ParseUser.getCurrentUser());
                readyForCheck.setBody(checkMessage);




            }
        });


    }


}
