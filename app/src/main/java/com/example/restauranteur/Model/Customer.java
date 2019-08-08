package com.example.restauranteur.Model;


import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class Customer {
    private ParseUser user;


    public Customer(ParseUser parseUser){
        user = parseUser;
    }

    public static Customer getCurrentCustomer(){
        ParseUser user = ParseUser.getCurrentUser();
        return new Customer(user);
    }

    public static void logOut(){
        ParseUser.logOut();
    }

    public void setUsername(String username){
        user.setUsername(username);
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

    public void put(String key, boolean value){
        user.put(key, value);
    }

    public void put(String key, CustomerInfo object){
        user.put(key, object);
    }

    ParseUser getParseUser(){
        return user;
    }

    public Visit getCurrentVisit(){
        return getInfo().getVisit();
    }

    public CustomerInfo getInfo(){
        return (CustomerInfo )user.getParseObject("customerInfo");
    }

    public void saveInBackground(SaveCallback callback){
        user.saveInBackground(callback);
    }
}
