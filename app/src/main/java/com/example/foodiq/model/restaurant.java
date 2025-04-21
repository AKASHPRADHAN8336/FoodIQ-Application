package com.example.foodiq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class restaurant implements Serializable {
    private String id;
    private String restaurantName;
    private String price;
    private String openeingTime;
    private String closingTime;
    private Map<String,String> imageUrl;



    public restaurant(){

    }

    public restaurant(String restaurantName, String id, String price, String openeingTime, String closingTime, Map<String, String> imageUrl) {
        this.restaurantName = restaurantName;
        this.id = id;
        this.price = price;
        this.openeingTime = openeingTime;
        this.closingTime = closingTime;
        this.imageUrl = imageUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpeneingTime() {
        return openeingTime;
    }

    public void setOpeneingTime(String openeingTime) {
        this.openeingTime = openeingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public Map<String, String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Map<String, String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrlAsList(){
        return new ArrayList<>(imageUrl.values());
    }
}
