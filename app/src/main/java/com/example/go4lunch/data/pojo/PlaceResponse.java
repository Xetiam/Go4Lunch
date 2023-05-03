package com.example.go4lunch.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceResponse {
    @SerializedName("business_status")
    String businessStatus;
    @SerializedName("formatted_phone_number")
    String formattedPhoneNumber;
    @SerializedName("geometry")
    GeometryResponse geometry;
    @SerializedName("name")
    String name;
    @SerializedName("opening_hours")
    PlaceOpeningHoursResponse openingHours;
    @SerializedName("photos")
    ArrayList<PlacePhotoResponse> photos;
    @SerializedName("place_id")
    String placeId;
    @SerializedName("vicinity")
    String vicinity;
    @SerializedName("website")
    String website;
}
