package com.example.restauranteur.simpleChat;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
        public static final String VISIT_KEY = "visit";
        public static final String BODY_KEY = "body";
        public static final String AUTHOR_ID_KEY = "author";

        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        public ParseUser getAuthor() {
        return getParseUser(AUTHOR_ID_KEY); }

        public void setAuthor(ParseUser author) {
        put(AUTHOR_ID_KEY, author); }

        public String getBody() {
        return getString(BODY_KEY);
    }

        public void setBody(String body) {
        put(BODY_KEY, body);
    }



}