package com.example.restauranteur.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Visit")
public class Visit extends ParseObject {

    private static final String KEY_TABLENUMBER = "tableNumber";
    private static final String KEY_CUSTOMER = "customer";
    private static final String KEY_SERVER = "server";

    // get and setter for table number
    public void setTableNumber(String tableNum) {
        put(KEY_TABLENUMBER, tableNum);
    }

    public String getTableNumber() {
        return getString(KEY_TABLENUMBER);
    }

    // get and setter for customer
    public void setCustomer(ParseUser customer) {
        put(KEY_CUSTOMER, customer);
    }

    public ParseUser getCustomer() {
        return getParseUser(KEY_CUSTOMER);
    }

    // get and setter for server
    public void setServer(ParseUser server) {
        put(KEY_SERVER, server);
    }

    public ParseUser getServer() {
        try {
            return fetchIfNeeded().getParseUser(KEY_SERVER);
        } catch (ParseException e) {
            Log.e("OOPS", "Something has gone terribly wrong with Parse", e);
            return null;
        }
    }

    //From the order class include the waiter table and customer table
    public static class Query extends ParseQuery<Visit> {
        public Query() {
            super(Visit.class);
        }

    }



}