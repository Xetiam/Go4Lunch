package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class RestaurantEntity implements Serializable {
    private final ArrayList<String> lunchers;
    private final String restaurantid;
    private final String restaurantname;
    private final String restaurantdescription;
    private final String openingHour;
    private final double restaurantpositionlat;
    private final double restaurantpositionlng;
    private final Long evaluation;
    private final String drawableUrl;

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

    public String getRestaurantId() {
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
        if(evaluation != null) {
            return evaluation;
        } else {
            return 0L;
        }
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }

    public ArrayList<String> getLunchers() {
        if(lunchers != null) {
            return lunchers;
        } else {
            return new ArrayList<>();
        }
    }

}
