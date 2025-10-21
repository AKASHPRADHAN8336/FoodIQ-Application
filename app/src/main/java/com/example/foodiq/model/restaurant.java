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
<<<<<<< HEAD
//    private String closingTime;
=======
    private String closingTime;
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
    private Map<String,String> imageUrl;



    public restaurant(){

    }

<<<<<<< HEAD
    public restaurant(String restaurantName, String id, String price, String openeingTime, Map<String, String> imageUrl) {
=======
    public restaurant(String restaurantName, String id, String price, String openeingTime, String closingTime, Map<String, String> imageUrl) {
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
        this.restaurantName = restaurantName;
        this.id = id;
        this.price = price;
        this.openeingTime = openeingTime;
<<<<<<< HEAD
//        this.closingTime = closingTime;
=======
        this.closingTime = closingTime;
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
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

<<<<<<< HEAD
//    public String getClosingTime() {
//        return closingTime;
//    }

//    public void setClosingTime(String closingTime) {
//        this.closingTime = closingTime;
//    }
=======
    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07

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
