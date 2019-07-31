package com.example.restauranteur.Model;

import com.parse.ParseClassName;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Installation")
public class Installation extends ParseInstallation {

    private ParseInstallation parseInstallation;


    private static final String KEY_CHANNELS = "channels";
    private static final String KEY_BADGE = "badge";

    public List<String> getChannels() {
        return getList(KEY_CHANNELS);
    }

    public void addChannle(String user){
        add(KEY_CHANNELS, user);
    }

    public void removeChannel(String user) {
        List<String> removedChannels = new ArrayList<>();
        removedChannels.add(user);
        removeAll(KEY_CHANNELS, removedChannels);
    }


    public void setBadgeNumber(int badge) {
        put(KEY_BADGE, badge);
    }

    public int getBadgeNumber() {
        return getInt(KEY_BADGE);
    }



}
