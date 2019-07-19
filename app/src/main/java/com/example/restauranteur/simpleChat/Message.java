package com.example.restauranteur.simpleChat;

import com.example.restauranteur.Customer;
import com.example.restauranteur.Visit;
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

        public void setVisit(Visit visit) {
            put(VISIT_KEY, visit); }

        public Customer getAuthor() {
            Customer author = (Customer) getParseUser(AUTHOR_ID_KEY);
            return author;
        }

        public void setAuthor(Customer author) {
        put(AUTHOR_ID_KEY, author); }

        public String getBody() {
        return getString(BODY_KEY);
    }

        public void setBody(String body) {
        put(BODY_KEY, body);
    }
}



