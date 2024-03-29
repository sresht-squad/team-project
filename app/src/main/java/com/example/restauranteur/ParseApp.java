package com.example.restauranteur;

import android.app.Application;

import com.example.restauranteur.Model.CustomerInfo;
import com.example.restauranteur.Model.Message;
import com.example.restauranteur.Model.ServerInfo;
import com.example.restauranteur.Model.Visit;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //let parse know about the model class subclass
        ParseObject.registerSubclass(Visit.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(ServerInfo.class);
        ParseObject.registerSubclass(CustomerInfo.class);


        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        //config the parse
        //get it running & connect
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("restauranteur")
                .clientKey("jesuskellynamitasresht")
                .server("https://fbu-restauranteur.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}