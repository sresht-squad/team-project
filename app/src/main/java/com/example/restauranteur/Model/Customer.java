package com.example.restauranteur.Model;


import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Customer {
   private ParseUser user;
   private String VISIT_KEY = "visit";
   private String ACTIVE_VISIT = "visits";


   public Customer(ParseUser parseUser){
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

    public static Customer getCurrentCustomer(){
        ParseUser user = ParseUser.getCurrentUser();
        return new Customer(user);
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

    public void setVisit(Visit visit) {user.put(VISIT_KEY, visit); }

    public Visit getCurrentVisit(){
       return (Visit)user.getParseObject(VISIT_KEY);
    }

    //array of visit
    public JSONArray getActiveVisits(){
       return getJSONArray(ACTIVE_VISIT);
    }


   public void ActiveVisit(Customer customer){
       user.add(ACTIVE_VISIT,customer);
   }

   public void deactivateVisit(Customer customer){
       List<Customer> customers = new ArrayList<>();
       customers.add(customer);
       removeAll(ACTIVE_VISIT, customers);
   }

   









}
