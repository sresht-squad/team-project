package com.example.restauranteur.Model;

public class MenuItem {
    private String name;
    private String price;
    private String description;
    private boolean heading;
    private boolean mainHeading;


    public String getName(){
        return name;
    }

    public void setName(String myName){
        name = myName;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String myPrice){
        price = myPrice;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String myDescription){
        description = myDescription;
    }

    public Boolean getHeading(){
        return heading;
    }

    public void setHeading(Boolean isHeading){
        heading = isHeading;
    }

    public Boolean getMainHeading(){
        return mainHeading;
    }

    public void setMainHeading(Boolean isMainHeading){
        mainHeading = isMainHeading;
    }
}
