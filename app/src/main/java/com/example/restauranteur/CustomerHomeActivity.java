package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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
    Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        logout = findViewById(R.id.ivLogout);
        btnTestCheck = findViewById(R.id.btnTestCheck);

        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                visit = new Visit();

                ParseUser currentCustomer = ParseUser.getCurrentUser();
                visit.setCustomer(currentCustomer);

                final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("serverId", serverId);

                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            Log.d("SIZE OF QUERY RESULT", Integer.toString(objects.size()));
                            ParseUser server = objects.get(0);
                            visit.setServer(server);
                            visit.setTableNumber("2");
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
                String getCheck = "Ready for check"; 

            }
        });
    }







}
