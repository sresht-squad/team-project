package com.example.restauranteur.Model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.reflect.Array.*;


@ParseClassName("Visit")
public class Visit extends ParseObject {

    private static final String KEY_TABLENUMBER = "tableNumber";
    private static final String KEY_CUSTOMERS = "customers";
    private static final String KEY_SERVER = "server";
    private static final String KEY_ACTIVE = "active";

    // get and setter for table number
    public void setTableNumber(String tableNum) {
        put(KEY_TABLENUMBER, tableNum);
    }

    public String getTableNumber() {
        return getString(KEY_TABLENUMBER);
    }

    // get and setter for customer
    public void addCustomer(Customer customer) {
        add(KEY_CUSTOMERS, customer.getParseUser());
    }

    public JSONArray getCustomers() {
        return getJSONArray("customers");
    }

    // get and setter for server
    public void setServer(Server server) {
        put(KEY_SERVER, server.getParseUser());
    }

    public Server getServer() {
        try {
            return new Server(fetchIfNeeded().getParseUser(KEY_SERVER));
        } catch (ParseException e) {
            Log.e("OOPS", "Something has gone terribly wrong with Parse", e);
            return null;
        }
    }

    public void setActive(boolean active){
        put(KEY_ACTIVE, active);
    }

    public Boolean getActive(){
       return getBoolean(KEY_ACTIVE);
    }

    //From the order class include the waiter table and customer table
    public static class Query extends ParseQuery<Visit> {
        public Query() {
            super(Visit.class);
        }
    }



}