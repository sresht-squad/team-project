package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
        public static final String VISIT_KEY = "visit";
        public static final String BODY_KEY = "body";
        public static final String AUTHOR_ID_KEY = "author";
        static final String STATUS = "active";

        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        public void setVisit(Visit visit) {
            put(VISIT_KEY, visit); }

        public void setActive(boolean tf) {
        put(STATUS, tf); }

        public boolean getActive() {
        return getBoolean(STATUS);}

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



