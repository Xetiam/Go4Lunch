package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatLngLiteralResponse {
    @JsonProperty("lat")
    Double lat;
    @JsonProperty("lng")
    Double lng;
}
