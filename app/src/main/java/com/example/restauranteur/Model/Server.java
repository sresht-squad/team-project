package com.example.restauranteur.Model;


import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private ParseUser user;
    private static final String ACTIVE_VISITS = "visits";
    
    
    public Server(ParseUser parseUser){
        user = parseUser;
    }

    public void setUsername(String username){
        user.setUsername(username);
    }

    public String getUsername(){
        return user.getUsername();
    }

    public void setPassword(String password){
        user.setPassword(password);
    }

    public void signUpInBackground(SignUpCallback callback){
        user.signUpInBackground(callback);
    }

    public static void logInInBackground(String username, String password, LogInCallback callback){
        ParseUser.logInInBackground(username, password, callback);
    }

    public void put(String key, String value){
        user.put(key, value);
    }

    public void put(String key, int value){
        user.put(key, value);
    }

    public void put(String key, boolean value){
        user.put(key, value);
    }

    public String getString(String key){
        return user.getString(key);
    }

    public static Server getCurrentServer(){
        ParseUser user = ParseUser.getCurrentUser();
        return new Server(user);
    }

    public static void logOut(){
        ParseUser.logOut();
    }

    public String getObjectId(){
        return user.getObjectId();
    }

    public ParseUser getParseUser(){
        return user;
    }

    public void setFirstName(String first){
        user.put("firstName", first);
    }

    public void setLastName(String last){
        user.put("lastName", last);
    }

    //getting the array from the Parse database
    public JSONArray getVisits(){
        return user.getJSONArray(ACTIVE_VISITS);
    }
    // adding the customer to the visits array
    public void addCustomerToVisit(Customer customer){
        user.add(ACTIVE_VISITS, customer);
    }
    // removing the customer from the visits array
    public void removeCustomer(Customer customer){
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        user.removeAll(ACTIVE_VISITS, customers);
    }


}