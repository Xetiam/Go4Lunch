package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeometryResponse {
    @JsonProperty("location")
    LatLngLiteralResponse location;
    @JsonProperty("viewport")
    BoundsResponse viewport;
}
