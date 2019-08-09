package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;


@ParseClassName("CustomerInfo")
public class CustomerInfo extends ParseObject {

    public Visit getVisit(){
        try {
            return (Visit) fetchIfNeeded().get("visit");
        } catch (ParseException e){
            return null;
        }
    }

    public void setVisit(Visit visit){
        put("visit", visit);
    }

}
