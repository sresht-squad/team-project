package com.example.restauranteur;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Visit")
public class Visit extends ParseObject {

    public static final String BODY_KEY = "body";
    public static final String SERVER_ID_KEY = "server";
    public static final String USER_ID_KEY = "user";
    public static final String KEY_TABLENUMBER = "tableNumber";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_SERVER = "waiter";

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

    // get and setter for waiter
    public void setServer(ParseUser server) {
        put(KEY_SERVER, server);
    }

    public ParseUser getWaiter() {
        return getParseUser(KEY_SERVER);
    }

    //From the order class include the waiter table and customer table
    public static class Query extends ParseQuery<Visit> {
        public Query() {
            super(Visit.class);
        }

        public Visit.Query withCustomer() {
            include("customer");
            return this;
        }

        public Visit.Query withWaiter() {
            include("waiter");
            return this;
        }

    }

    public String getServer() {
        return getString(SERVER_ID_KEY);
    }

    public String getUser() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }



}
