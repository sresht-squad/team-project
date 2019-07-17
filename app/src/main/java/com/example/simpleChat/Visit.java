package com.example.simpleChat;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Visit")
public class Visit extends ParseObject {

    public static final String BODY_KEY = "body";
    public static final String CUSTOMER_ID_KEY = "customer";
    public static final String SERVER_ID_KEY = "server";
    public static final String USER_ID_KEY = "user";

    public String getCustomer() {
        return getString(CUSTOMER_ID_KEY);
    }

    public String getServer() {
        return getString(SERVER_ID_KEY);
    }

    public String getUser() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }



}
