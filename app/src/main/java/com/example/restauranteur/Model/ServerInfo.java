package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


@ParseClassName("ServerInfo")
public class ServerInfo extends ParseObject {

    private static final String KEY_VISITS = "visits";


    public List<Visit> getVisits() {
        return getList("visits");
    }

    ArrayList<Visit> getVisitsFetch() {
        try {
            return (ArrayList<Visit>) fetchIfNeeded().get("visits");
        } catch (ParseException e){
            return null;
        }
    }

    JSONArray getVisitsJSON() {
        try {
            return fetch().getJSONArray("visits");
        } catch (ParseException e){
            return null;
        }
    }

    public void addVisit(Visit visit){
        add(KEY_VISITS, visit);
    }

    public void removeVisit(Visit visit) {
        List<Visit> removedVisits = new ArrayList<>();
        removedVisits.add(visit);
        removeAll(KEY_VISITS, removedVisits);
    }

    public String getRestaurantId(){
        try {
            return fetchIfNeeded().getString("restaurantId");
        } catch (ParseException e) {
            return "";
        }
    }







}
