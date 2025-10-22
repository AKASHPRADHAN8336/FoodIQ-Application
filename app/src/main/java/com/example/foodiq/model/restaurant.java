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
    private Map<String, String> imageUrl;

    // Add new fields for restaurant details
    private String cuisineType;
    private String deliveryTime;
    private String rating;
    private String address;
    private String phone;
    private String website;
    private String distance;

    public restaurant() {
        // Default constructor required for Firebase
    }

    public restaurant(String restaurantName, String id, String price, String openeingTime,
                      String closingTime, Map<String, String> imageUrl, String cuisineType,
                      String deliveryTime, String rating, String address, String phone,
                      String website, String distance) {
        this.restaurantName = restaurantName;
        this.id = id;
        this.price = price;
        this.openeingTime = openeingTime;
        this.closingTime = closingTime;
        this.imageUrl = imageUrl;
        this.cuisineType = cuisineType;
        this.deliveryTime = deliveryTime;
        this.rating = rating;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.distance = distance;
    }

    // Getters and Setters for all fields
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

    public List<String> getImageUrlAsList() {
        return new ArrayList<>(imageUrl.values());
    }

    // New getters and setters
    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}