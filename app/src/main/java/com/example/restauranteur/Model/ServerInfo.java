package com.example.restauranteur.Model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;


@ParseClassName("ServerInfo")
public class ServerInfo extends ParseObject {

    private static final String KEY_VISITS = "visits";


    public JSONArray getVisits(){
        try {
            return fetchIfNeeded().getJSONArray("visits");
        }catch (ParseException e) {
            Log.e("Parse Error", "Something wrong with Parse", e);
            return null;
        }
    }

    public void addVisit(Visit visit){
        add("visits", visit);
    }



}
