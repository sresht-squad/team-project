package com.example.restauranteur.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Server")
public class Server extends ParseUser {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SERVERID = "serverId";

    public void setServerId(String serverId) {
        put(KEY_SERVERID, serverId);
    }

    public String getServerId() {
        return getString(KEY_SERVERID);
    }

}
