package com.example.restauranteur;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("ServerID")
public class ServerID extends ParseObject {
    public static final String KEY_SERVER = "server";
    public static final String KEY_ID_NUMBER = "idNumber";

    public void setServer(ParseUser server) {
        put(KEY_SERVER, server);
    }

    public ParseUser getServer() {
        return getParseUser(KEY_SERVER);
    }

    public void setIdNumber(String idNumber) {
        put(KEY_ID_NUMBER, idNumber);
    }

    public String getIdNumber() {
        return getString(KEY_ID_NUMBER);
    }

    public static class Query extends ParseQuery<ServerID> {
        public Query() {
            super(ServerID.class);
        }

        public Query getServerWithID(String idNumber){
            whereEqualTo("idNumber", idNumber);
            return this;
        }
    }




}
