package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;


@ParseClassName("ServerInfo")
public class ServerInfo extends ParseObject {

    private static final String KEY_VISITS = "visits";


    public List<Visit> getVisit() {
        return getList("visits");
    }

    public void addVisit(Visit visit){
        add(KEY_VISITS, visit);
    }


}
