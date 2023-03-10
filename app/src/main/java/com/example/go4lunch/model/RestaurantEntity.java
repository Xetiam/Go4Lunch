package com.example.go4lunch.model;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantEntity {
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("description")
    @Expose
    private final String description;
    //private timeOpening;
    @SerializedName("position")
    @Expose
    private final LatLng position;
    @SerializedName("coworkerStars")
    @Expose
    private final List<Integer> coworkerStars;
    @SerializedName("drawableUrl")
    @Expose
    private final String drawableUrl;

    public RestaurantEntity(String id,
                            String name,
                            String description,
                            LatLng position,
                            List<Integer> coworkerStars,
                            String drawableUrl) {
        this.id = id;
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

    public String getId() {
        return id;
    }
}
