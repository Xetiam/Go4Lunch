package com.example.go4lunch.data.pojo;


import com.example.go4lunch.model.RestaurantEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlacesNearbySearchResponse {
    @JsonProperty("html_attributions")
    @Expose
    ArrayList<String> attributions;
    @JsonProperty("results")
    @Expose
    ArrayList<PlaceResponse> results;
    @JsonProperty("status")
    @Expose
    String status;
    @JsonProperty("error_message")
    @Expose
    String error;
    @JsonProperty("info_messages")
    @Expose
    ArrayList<String> infos;
    @JsonProperty("next_page_token")
    @Expose
    String nextPageToken;
    public List<RestaurantEntity> formatPlaces() {
        List<RestaurantEntity> formattedPlaces = new ArrayList<>();
        for(PlaceResponse place : this.results) {
            formattedPlaces.add(new RestaurantEntity(place.placeId,
                    place.name,
                    place.address,
                    new LatLng(place.geometry.location.lat, place.geometry.location.lng),
                    Arrays.asList(1, 2, 3, 4),
                    ""));
        }
        return formattedPlaces;
    }
}