package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class RestaurantEntity {
    private String restaurantid;
    private String restaurantname;
    private String restaurantdescription;
    private String openingHour;
    private GeoPoint restaurantposition;
    private Long evaluation;
    private String drawableUrl;

    public RestaurantEntity(String restaurantid,
                            String restaurantname,
                            String restaurantdescription,
                            String openingHour,
                            GeoPoint restaurantposition,
                            Long evaluation,
                            String drawableUrl) {
        this.restaurantid = restaurantid;
        this.restaurantname = restaurantname;
        this.restaurantdescription = restaurantdescription;
        this.openingHour = openingHour;
        this.restaurantposition = restaurantposition;
        this.evaluation = evaluation;
        this.drawableUrl = drawableUrl;
    }

    public RestaurantEntity() {}

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
        return new LatLng(restaurantposition.getLatitude(), restaurantposition.getLongitude());
    }

    public void setRestaurantposition(GeoPoint restaurantposition) {
        this.restaurantposition = restaurantposition;
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
}
