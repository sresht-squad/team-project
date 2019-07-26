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

    public ArrayList<Visit> getVisitsFetch() {
        try {
            return (ArrayList<Visit>) fetchIfNeeded().get("visits");
        } catch (ParseException e){
            return null;
        }
    }

    public JSONArray getVisitsJSON() {
        try {
            return fetchIfNeeded().getJSONArray("visits");
        } catch (ParseException e){
            return null;
        }
    }

    public void addVisit(Visit visit){
        add(KEY_VISITS, visit);
    }


}
