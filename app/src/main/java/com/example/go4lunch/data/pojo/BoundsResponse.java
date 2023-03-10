package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundsResponse {
    @JsonProperty("northeast")
    LatLngLiteralResponse northeast;
    @JsonProperty("southwest")
    LatLngLiteralResponse southwest;
}
