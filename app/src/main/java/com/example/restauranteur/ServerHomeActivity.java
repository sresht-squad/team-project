package com.example.restauranteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class ServerHomeActivity extends AppCompatActivity {

    TextView tvId;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);

        //matching server to customer

        tvId = findViewById(R.id.tvId);

        logout = findViewById(R.id.ivLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(ServerHomeActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        tvId.setText(currentUser.getString("serverId"));

//        //query for server who the serverID points to
//        final ServerID.Query serverIdQuery = new ServerID.Query();
//        serverIdQuery.getServerWithID(serverId);
//
//        serverIdQuery.findInBackground(new FindCallback<ServerID>() {
//            @Override
//            public void done(List<ServerID> objects, ParseException e) {
//                if (e == null) {
//                    Log.d("create new visit", "serverID retrieval success");
//                    ParseUser server = objects.get(0).getServer();
//                    visit.setServer(server);
//                    visit.setTableNumber("2");
//
//                    visit.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null){
//                                Log.d("Saving","Error while saving");
//                                e.printStackTrace();
//                                return;
//                            }else{
//                                Log.d("Saving", "success");
//                            }
//                        }
//                    });
//
//                } else {
//                    Log.d("create new visit", "serverID retrieval failure");
//                    e.printStackTrace();
//                }
//            }
//        });
    }


}
