package com.example.restauranteur.Model;


import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.ArrayList;

public class Server {
    private ParseUser user;
    private String VISIT_KEY = "visit";
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
    //create an empty array in the server's visit array
    public void put(String key, ArrayList<Visit> visits ){
        user.put(key, visits); 
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

    // setting up the list of visits for the Server
    public JSONArray getVisits(){
        try {
            return user.fetchIfNeeded().getJSONArray(ACTIVE_VISITS);
        }catch (ParseException e) {
            Log.e("Parse Err.", "Parse Database Not Working", e);
            return null;
        }
    }

    public void addVisit(Visit visit){
        user.add("visits", visit);
    }
    
    
    //Server is able to set vist and getCurrentVisit
    public void setVisit(Visit visit) {user.put(VISIT_KEY, visit); }

    public Visit getCurrentVisit(){
        return (Visit)user.getParseObject(VISIT_KEY);
    }




}