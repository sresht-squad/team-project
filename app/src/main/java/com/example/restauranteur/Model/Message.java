package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Message")
public class Message extends ParseObject {
        public static final String VISIT_KEY = "visit";
        public static final String BODY_KEY = "body";
        public static final String AUTHOR_ID_KEY = "author";
        public static final String ACTIVE_VISITS = "visits";
        static final String STATUS = "active";


        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        public void setVisit(Visit visit) {
            put(VISIT_KEY, visit); }

        public void setActive(boolean tf) {
        put(STATUS, tf); }

        public boolean getActive() {
        return getBoolean(STATUS);}

        public Customer getAuthor() {
            return new Customer(getParseUser(AUTHOR_ID_KEY));
        }

        public void setAuthor(Customer author) {
        put(AUTHOR_ID_KEY, author.getParseUser()); }

        public String getBody() {
        return getString(BODY_KEY);
    }

        public void setBody(String body) {
        put(BODY_KEY, body);
    }

        
        //getting the array from the Parse database
        public JSONArray getVisits(){
            return getJSONArray(ACTIVE_VISITS);
        }
        // adding the customer to the visits array
        public void addCustomerToVisit(Customer customer){
            add(ACTIVE_VISITS, customer);
        }
        // removing the customer from the visits array
        public void removeCustomer(Customer customer){
            List<Customer> customers = new ArrayList<>();
            customers.add(customer);
            removeAll(ACTIVE_VISITS, customers);
        }

        // checking to see if the customer is already in the current visit array.
        public boolean isNotCustomer (Customer customer) {
            JSONArray customers = getVisits();
            if (customers != null){
                for (int i = 0 ; i < customers.length() ;i++ ){
                    try{
                        if (customers.getJSONObject(i).getString("objectId")
                                .equals(customer.getObjectId())){
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            return true;
        }


}



