package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.restauranteur.fragment.CustomerChatFragment;
import com.example.restauranteur.fragment.CustomerQuickRequestFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class CustomerHomeActivity extends AppCompatActivity {

    EditText etServerId;
    Button btnCreateVisit;
    ImageView logout;
    Button btnTestCheck;
    EditText etTableNumber;
    BottomNavigationView customerBottomNavigation;
    Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        customerBottomNavigation = findViewById(R.id.bottom_navigation);


        customerBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = new CustomerQuickRequestFragment();

                switch (menuItem.getItemId()) {
                    case R.id.action_request:
                        fragment = new CustomerQuickRequestFragment();
                        Toast.makeText(CustomerHomeActivity.this, "fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_visit:
                        fragment = new CustomerChatFragment();
                        Toast.makeText(CustomerHomeActivity.this, "fragment", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
        });

        logout = findViewById(R.id.ivLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(CustomerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });


        //Not deleted because this has to do with the activity in which the customer enters table number and server id//

        /*etServerId = findViewById(R.id.etServerId);
        btnCreateVisit = findViewById(R.id.btnCreateVisit);
        logout = findViewById(R.id.ivLogout);
        btnTestCheck = findViewById(R.id.btnCheck);
        etTableNumber = findViewById(R.id.etTableNumber);*/

        /*//create a new visit
        btnCreateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverId = etServerId.getText().toString();
                String tableNum = etTableNumber.getText().toString();
                visit = new Visit();

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
                            int size_of_query_result = Log.d("SIZE OF QUERY RESULT", Integer.toString(objects.size()));
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

    }*/


    }
}
