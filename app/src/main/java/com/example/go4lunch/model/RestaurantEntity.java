package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantEntity implements Serializable {
    private ArrayList<String> lunchers;
    private String restaurantid;
    private String restaurantname;
    private String restaurantdescription;
    private String openingHour;
    private double restaurantpositionlat;
    private double restaurantpositionlng;
    private Long evaluation;
    private String drawableUrl;

    public RestaurantEntity(String restaurantid,
                            String restaurantname,
                            String restaurantdescription,
                            String openingHour,
                            GeoPoint restaurantposition,
                            Long evaluation,
                            String drawableUrl,
                            ArrayList<String> lunchers) {
        this.restaurantid = restaurantid;
        this.restaurantname = restaurantname;
        this.restaurantdescription = restaurantdescription;
        this.openingHour = openingHour;
        this.restaurantpositionlat = restaurantposition.getLatitude();
        this.restaurantpositionlng = restaurantposition.getLongitude();
        this.evaluation = evaluation;
        this.drawableUrl = drawableUrl;
        this.lunchers = lunchers;
    }

    public String getRestaurantid() {
        return restaurantid;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public String getRestaurantdescription() {
        return restaurantdescription;
    }

    public LatLng getRestaurantposition() {
        return new LatLng(restaurantpositionlat, restaurantpositionlng);
    }

    public Long getEvaluation() {
        return evaluation;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }

    public ArrayList<String> getLunchers() {
        return lunchers;
    }

}
