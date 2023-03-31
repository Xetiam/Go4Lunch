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

    public RestaurantEntity() {
    }

    public String getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(String restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getRestaurantname() {
        return restaurantname;
    }

    public void setRestaurantname(String restaurantname) {
        this.restaurantname = restaurantname;
    }

    public String getRestaurantdescription() {
        return restaurantdescription;
    }

    public void setRestaurantdescription(String restaurantdescription) {
        this.restaurantdescription = restaurantdescription;
    }

    public LatLng getRestaurantposition() {
        return new LatLng(restaurantpositionlat, restaurantpositionlng);
    }

    public void setRestaurantposition(GeoPoint restaurantposition) {
        this.restaurantpositionlat = restaurantposition.getLatitude();
        this.restaurantpositionlng = restaurantposition.getLongitude();
    }

    public Long getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Long evaluation) {
        this.evaluation = evaluation;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }

    public void setDrawableUrl(String drawableUrl) {
        this.drawableUrl = drawableUrl;
    }

    public ArrayList<String> getLunchers() {
        return lunchers;
    }

    public void setLunchers(ArrayList<String> lunchers) {
        this.lunchers = lunchers;
    }
}
