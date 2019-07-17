package com.example.restauranteur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnCreateVisit;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        logout = findViewById(R.id.ivLogout);

        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                ParseUser currentCustomer = ParseUser.getCurrentUser();
                final Visit visit = new Visit();
                visit.setCustomer(currentCustomer);

                //query for server who the serverID points to
                final ServerID.Query serverIdQuery = new ServerID.Query();
                serverIdQuery.getServerWithID(serverId);

                serverIdQuery.findInBackground(new FindCallback<ServerID>() {
                    @Override
                    public void done(List<ServerID> objects, ParseException e) {
                        if (e == null) {
                            Log.d("create new visit", "serverID retrieval success");
                            ParseUser server = objects.get(0).getServer();
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
                            Log.d("create new visit", "serverID retrieval failure");
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
    }

}
