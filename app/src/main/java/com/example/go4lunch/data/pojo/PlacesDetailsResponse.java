package com.example.go4lunch.data.pojo;

import com.example.go4lunch.model.RestaurantDetailEntity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlacesDetailsResponse {
    @SerializedName("html_attributions")
    ArrayList<String> attributions;
    @SerializedName("result")
    PlaceResponse result;
    @SerializedName("status")
    String status;
    @SerializedName("error_message")
    String error;
    @SerializedName("info_messages")
    ArrayList<String> infos;

    public RestaurantDetailEntity toDomain() {
        String websiteUrl = "";
        String formattedPhoneNumber = "";
        ArrayList<String> weekText = new ArrayList<>();
        String restaurantId = "";
        PlaceResponse place = this.result;
        if (place != null) {
            if (place.website != null) {
                websiteUrl = place.website;
            }
            if (place.formattedPhoneNumber != null) {
                formattedPhoneNumber = place.formattedPhoneNumber;
            }
            if (place.openingHours.weekdayText != null) {
                weekText = place.openingHours.weekdayText;
            }
            if(place.placeId != null) {
                restaurantId = place.placeId;
            }
        }
        return new RestaurantDetailEntity(websiteUrl, formattedPhoneNumber, weekText, restaurantId);
    }
}
