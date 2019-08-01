package com.example.restauranteur.Model;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private ParseUser user;
    private static final String SERVER_INFO = "serverInfo";
    private static final String SERVER_INSTALLATION = "installation";

    
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

    public void saveInBackground(SaveCallback callback){
        user.saveInBackground(callback);
    }
    // Being able to put Server data into Parse
    public void put(String key, String value){
        user.put(key, value);
    }

    public void put(String key, int value){
        user.put(key, value);
    }

    public void put(String key, boolean value){
        user.put(key, value);
    }

    public void put(String key, ParseObject value){
        user.put(key, value);
    }

    public void put(String key, ArrayList<Visit> value){
        user.put(key, value);
    }

    public void put(String key, ServerInfo serverInfo){
        user.put(key,serverInfo);
    }

    public void put(String key, Installation installation){
        user.put(key,installation);
    }


    public void addVisit(Visit visit){
        user.add("visits", visit);
    }

    //being able to get the serverInfo object
    public ServerInfo getServerInfo(){
        try {
            return (ServerInfo) user.fetchIfNeeded().getParseObject("serverInfo");
        } catch (ParseException e) {
            return null;
        }
    }

    //being able to get the server Installation
    public ParseInstallation getServerInstallation(){
        return (ParseInstallation) user.getParseObject("installation");
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
    public List<Visit> getVisits(){
        ServerInfo info = getServerInfo();
        return info.getVisitsFetch();
    }

    public JSONArray getVisitsJSON(){
        ServerInfo info = getServerInfo();
        return info.getVisitsJSON();
    }

    // adding the customer to the visits array
    /*
    public void addCustomerToVisit(Visit visit){
        user.add(ACTIVE_VISITS, visit);
    }
    // removing the customer from the visits array
    public void removeCustomer(Customer visit){
        ArrayList<Customer> visits = new ArrayList<>();
        visits.add(visit);
        user.removeAll(ACTIVE_VISITS, visits);
    }
    */


}