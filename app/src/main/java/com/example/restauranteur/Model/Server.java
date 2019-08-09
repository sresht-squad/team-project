package com.example.restauranteur.Model;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import org.json.JSONArray;
import java.util.List;

public class Server {
    private ParseUser user;

    
    public Server(ParseUser parseUser){
        user = parseUser;
    }

    public void setUsername(String username){
        user.setUsername(username);
    }

    public void setPassword(String password){
        user.setPassword(password);
    }

    public void signUpInBackground(SignUpCallback callback){
        user.signUpInBackground(callback);
    }

    public void saveInBackground(SaveCallback callback){
        user.saveInBackground(callback);
    }
    // Being able to put Server data into Parse

    public void put(String key, boolean value){
        user.put(key, value);
    }

    public void put(String key, ParseObject value){
        user.put(key, value);
    }

    public void put(String key, ServerInfo serverInfo){
        user.put(key,serverInfo);
    }


    //being able to get the serverInfo object
    public ServerInfo getServerInfo(){
        try {
            return (ServerInfo) user.fetchIfNeeded().getParseObject("serverInfo");
        } catch (ParseException e) {
            return null;
        }
    }


    public String getString(String key){
        return user.getString(key);
    }

    public String getObjectId(){
        return user.getObjectId();
    }

    public static Server getCurrentServer(){
        ParseUser user = ParseUser.getCurrentUser();
        return new Server(user);
    }

    public static void logOut(){
        ParseUser.logOut();
    }

    ParseUser getParseUser(){
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

}