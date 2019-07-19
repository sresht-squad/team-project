package com.example.restauranteur.simpleChat;

import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Visit;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

        public static final String VISIT_KEY = "visit";
        public static final String BODY_KEY = "body";
        public static final String AUTHOR_ID_KEY = "author";

        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        public void setVisit(Visit visit) {
            put(VISIT_KEY, visit); }

        public ParseUser getAuthor() {
            return getParseUser(AUTHOR_ID_KEY);
        }

        public void setAuthor(ParseUser author) {
        put(AUTHOR_ID_KEY, author); }

        public String getBody() {
        return getString(BODY_KEY);
    }

        public void setBody(String body) {
        put(BODY_KEY, body);
    }



    public static class Query extends ParseQuery<Message> {
        public Query() {
            super(Message.class);
        }

        public Query withUser(){
            include("vist");
            return this;
        }

    }
}



