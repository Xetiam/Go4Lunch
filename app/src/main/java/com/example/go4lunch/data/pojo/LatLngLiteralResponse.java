package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class LatLngLiteralResponse {
    @SerializedName("lat")
    Double lat;
    @SerializedName("lng")
    Double lng;
}
