package com.example.restauranteur.Model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;


@ParseClassName("CustomerInfo")
public class CustomerInfo extends ParseObject {

    private static final String KEY_VISITS = "Visits";

    public JSONArray getVisit(){
        try {
            return fetchIfNeeded().getJSONArray("messages");
        }catch (ParseException e) {
            Log.e("Parse Error", "Something has gone terribly wrong with Parse", e);
            return null;
        }
    }


    public void setVisit(Visit visit){
        put("visit", visit);
    }

}
