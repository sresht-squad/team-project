package com.example.restauranteur.Model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONObject;


@ParseClassName("CustomerInfo")
public class CustomerInfo extends ParseObject {

    public Visit getVisit(){
        try {
            return (Visit) fetchIfNeeded().get("visit");
        } catch (ParseException e){
            return null;
        }
    }


    public void setVisit(Visit visit){
        put("visit", visit);
    }

}
