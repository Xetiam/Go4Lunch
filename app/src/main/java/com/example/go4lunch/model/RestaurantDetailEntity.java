package com.example.go4lunch.model;

import java.util.ArrayList;

public class RestaurantDetailEntity {
    private String websiteUrl;
    private String formattedPhoneNumber;
    private ArrayList<String> weekText;
    private String restaurantId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public RestaurantDetailEntity(String websiteUrl, String formattedPhoneNumber, ArrayList<String> weekText, String restaurantId) {
        this.websiteUrl = websiteUrl;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.weekText = weekText;
        this.restaurantId = restaurantId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public ArrayList<String> getWeekText() {
        return weekText;
    }

    public void setWeekText(ArrayList<String> weekText) {
        this.weekText = weekText;
    }
}
