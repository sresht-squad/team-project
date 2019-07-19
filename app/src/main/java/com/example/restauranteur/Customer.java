package com.example.restauranteur;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Customer")
public class Customer extends ParseUser {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";

//    public void setUsername(String username) {
//        put(KEY_USERNAME, username);
//    }
//
//    public String getUsername() {
//        return getString(KEY_USERNAME);
//    }


}
