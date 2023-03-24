package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class BoundsResponse {
    @SerializedName("northeast")
    LatLngLiteralResponse northeast;
    @SerializedName("southwest")
    LatLngLiteralResponse southwest;
}
