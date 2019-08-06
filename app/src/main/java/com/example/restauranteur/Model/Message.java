package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@ParseClassName("Message")
public class Message extends ParseObject {

        public static final String BODY_KEY = "body";
        public static final String AUTHOR_ID_KEY = "author";
        final static String STATUS = "active";
        public String tableNum = "0";

        public Message(){}

        public void setActive(boolean tf) {
        put(STATUS, tf); }

        public boolean getActive() {
            try{
                return fetch().getBoolean(STATUS);
            }
            catch (ParseException e){
                return false;
            }

        }

        public Customer getAuthor() {
            return new Customer(getParseUser(AUTHOR_ID_KEY));
        }

        public void setAuthor(Customer author) {
        put(AUTHOR_ID_KEY, author.getParseUser()); }

        public String getBody() {
                return getString(BODY_KEY);
        }

        public void setBody(String body) {
            put(BODY_KEY, body);
        }



}



