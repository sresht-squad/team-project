package com.example.restauranteur.Models;

import com.parse.ParseUser;

public class Customer extends ParseUser {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";

    public String getUsername(){
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username){
        put(username, KEY_USERNAME);
    }

    public String getPassword(){
        return getString(KEY_USERNAME);
    }

    public void setPassword(String password){
        put(password, KEY_PASSWORD);
    }

    public String getName(){
        return getString(KEY_NAME);
    }

    public void setName(String name){
        put(name, KEY_NAME);
    }


}
