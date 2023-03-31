package com.example.go4lunch.data.pojo;


import com.example.go4lunch.model.RestaurantEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class PlacesNearbySearchResponse {
    @SerializedName("html_attributions")
    ArrayList<String> attributions;
    @SerializedName("results")
    ArrayList<PlaceResponse> results;
    @SerializedName("status")
    String status;
    @SerializedName("error_message")
    String error;
    @SerializedName("info_messages")
    ArrayList<String> infos;
    @SerializedName("next_page_token")
    String nextPageToken;

    public List<RestaurantEntity> toDomain() {
        List<RestaurantEntity> formattedPlaces = new ArrayList<>();
        String restaurantDescritption;
        String restaurantName;
        GeoPoint restaurantGeoPoint;
        String restaurantPhoto;
        String restaurantOpeningHour;
        for (PlaceResponse place : this.results) {
            restaurantDescritption = "";
            restaurantName = "";
            restaurantGeoPoint = null;
            restaurantPhoto = "";
            if (place.businessStatus.contains("OPERATIONAL")) {
                if (place.openingHours != null && place.openingHours.openNow) {
                    restaurantOpeningHour = "Ouvert";
                } else {
                    restaurantOpeningHour = "Fermé";
                }
                if (place.vicinity != null) {
                    restaurantDescritption = "Type - " + place.vicinity.substring(0, place.vicinity.indexOf(",") - 1);
                }
                if (place.name != null) {
                    restaurantName = place.name;
                }
                if (place.geometry.location != null) {
                    restaurantGeoPoint = new GeoPoint(place.geometry.location.lat, place.geometry.location.lng);
                }
                if (place.photos != null) {
                    restaurantPhoto = place.photos.get(0).photoReference;
                }
                formattedPlaces.add(new RestaurantEntity(place.placeId,
                        restaurantName,
                        restaurantDescritption,
                        restaurantOpeningHour,
                        restaurantGeoPoint,
                        0L,
                        restaurantPhoto,
                        new ArrayList<>()
                ));
            }
        }
        return formattedPlaces;
    }
}