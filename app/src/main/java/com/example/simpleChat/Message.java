package com.example.simpleChat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
        public static final String VISIT_KEY = "visit";
        public static final String BODY_KEY = "body";
        public static final String CUSTOMER_ID_KEY = "customer";
        public static final String SERVER_ID_KEY = "server";

        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        public String getBody() {
        return getString(BODY_KEY);
    }

        public void setCustomerId(String customer) {
        put(CUSTOMER_ID_KEY, customer); }

         public void setServerIdKey(String server) {
        put(SERVER_ID_KEY, server); }

        public void setBody(String body) {
        put(BODY_KEY, body);
    }



}
