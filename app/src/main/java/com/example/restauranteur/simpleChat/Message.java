package com.example.restauranteur.simpleChat;

import com.example.restauranteur.models.Customer;
import com.example.restauranteur.models.Visit;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
        private static final String VISIT_KEY = "visit";
        private static final String BODY_KEY = "body";
        private static final String AUTHOR_ID_KEY = "author";

        public ParseObject getVisit() {
        return getParseObject(VISIT_KEY); }

        void setVisit(Visit visit) {
            put(VISIT_KEY, visit); }

        Customer getAuthor() {
            Customer author = (Customer) getParseUser(AUTHOR_ID_KEY);
            return author;
        }

        void setAuthor(Customer author) {
        put(AUTHOR_ID_KEY, author); }

        String getBody() {
        return getString(BODY_KEY);
    }

        void setBody(String body) {
        put(BODY_KEY, body);
    }
}



