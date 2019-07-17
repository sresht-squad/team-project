package com.example.restauranteur.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Order")
public class Order extends ParseObject {

    public static final String KEY_TABLENUMBER = "tableNumber";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_WAITER = "waiter";

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
    public void setWaiter(ParseUser waiter) {
        put(KEY_WAITER, waiter);
    }

    public ParseUser getWaiter() {
        return getParseUser(KEY_WAITER);
    }


    //From the order class include the waiter table and customer table
    public static class Query extends ParseQuery<Order> {
        public Query() {
            super(Order.class);
        }

        public Query withCustomer() {
            include("customer");
            return this;
        }

        public Query withWaiter() {
            include("waiter");
            return this;
        }

    }
}
