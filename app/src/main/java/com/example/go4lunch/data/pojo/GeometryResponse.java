package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class GeometryResponse {
    @SerializedName("location")
    LatLngLiteralResponse location;
    @SerializedName("viewport")
    BoundsResponse viewport;
}
