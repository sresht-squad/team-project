package com.example.restauranteur.Model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;


@ParseClassName("ServerInfo")
public class CustomerInfo extends ParseObject {

    private static final String KEY_VISITS = "Visits";
    private static final String KEY_SERVERID = "server";


    public void setServerId(Server server) {
        put(KEY_SERVERID, server.getParseUser());
    }

    public Server getServerId() {
        try {
            return new Server(fetchIfNeeded().getParseUser(KEY_SERVERID));
        } catch (ParseException e) {
            Log.e("OOPS", "Something has gone terribly wrong with Parse", e);
            return null;
        }
    }

    public JSONArray getVisits(){
        try {
            return fetchIfNeeded().getJSONArray("messages");
        }catch (ParseException e) {
            Log.e("Parse Error", "Something has gone terribly wrong with Parse", e);
            return null;
        }
    }

    public void addVisit(Visit visit){
        add("Visits", visit);
    }



}
