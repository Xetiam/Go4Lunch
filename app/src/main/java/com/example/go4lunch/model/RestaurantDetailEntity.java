package com.example.go4lunch.model;

import java.util.ArrayList;

public class RestaurantDetailEntity {
    private String websiteUrl;
    private String formattedPhoneNumber;
    private ArrayList<String> weekText;
    private String restaurantId;

    public RestaurantDetailEntity(String websiteUrl, String formattedPhoneNumber, ArrayList<String> weekText, String restaurantId) {
        this.websiteUrl = websiteUrl;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.weekText = weekText;
        this.restaurantId = restaurantId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

}
