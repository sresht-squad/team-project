package com.example.restauranteur.Model;


import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Customer {
   private ParseUser user;
   private String VISIT_KEY = "visit";
   private String VISITS = "visits";


   public Customer(ParseUser parseUser){
       user = parseUser;
   }

   public void setUsername(String username){
       user.setUsername(username);
   }

   public String getUsername(){
       return user.getUsername();
   }

   public void setFirstName(String first){
        user.put("firstName", first);
    }

    public void setLastName(String last){
        user.put("lastName", last);
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

    public void put(String key, CustomerInfo object){
        user.put(key, object);
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

    public Visit getCurrentVisit(){
       return (Visit)user.getParseObject(VISIT_KEY);
    }

    public CustomerInfo getInfo(){
        return (CustomerInfo )user.getParseObject("customerInfo");
    }

    public void saveInBackground(SaveCallback callback){
        user.saveInBackground(callback);
    }










}
