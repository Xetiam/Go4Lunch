package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

//TODO: Terminer la construction de l'objet lorsque j'en saurais plus sur les retours de GooglePlaces
public class RestaurantEntity {
    private final String name;
    private final String description;
    //private timeOpening;
    private final LatLng position;
    private final List<Integer> coworkerStars;
    private final String drawableUrl;

    public RestaurantEntity(String name,
                            String description,
                            LatLng position,
                            List<Integer> coworkerStars, String drawableUrl) {
        this.name = name;
        this.description = description;
        this.position = position;
        this.coworkerStars = coworkerStars;
        this.drawableUrl = drawableUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getPosition() {
        return position;
    }

    public List<Integer> getCoworkerStars() {
        return coworkerStars;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }
}
