package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatLngLiteralResponse {
    @JsonProperty("lat")
    int lat;
    @JsonProperty("lng")
    int lng;
}
