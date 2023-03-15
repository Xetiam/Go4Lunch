package com.example.go4lunch.data.pojo;


import com.example.go4lunch.model.RestaurantEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
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
    public List<RestaurantEntity> toDomain() {
        List<RestaurantEntity> formattedPlaces = new ArrayList<>();
        for(PlaceResponse place : this.results) {
            String restaurantDescritption;
            //TODO : Il manque des données dans le retour de l'appel (googlemap a plus d'info)
            if(place.editorialSummary != null) {
                restaurantDescritption = place.editorialSummary.overview+" - "+place.vicinity;
            } else {
                restaurantDescritption = place.vicinity;
            }

            String openingHour;
            if(place.openingHours.openNow){//TODO : ne pas formatter le message ici et passé toute les horaire à Firestore
                openingHour = "Ouvert jusqu'à " /*formatter heure de fermeture + soon close en string*/;
            } else {
                openingHour = "Fermé";
            }

            String drawableUrl;
            if(place.photos.get(0) != null) {
                drawableUrl = place.photos.get(0).photoReference;
            } else {
                drawableUrl = "";
            }
            formattedPlaces.add(new RestaurantEntity(place.placeId,
                    place.name,
                    restaurantDescritption,
                    openingHour,
                    new GeoPoint(place.geometry.location.lat, place.geometry.location.lng),
                    0L,
                    drawableUrl));
        }
        return formattedPlaces;
    }
}