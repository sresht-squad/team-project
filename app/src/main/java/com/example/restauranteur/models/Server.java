package com.example.restauranteur.models;


import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Server {
    private ParseUser user;

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


}