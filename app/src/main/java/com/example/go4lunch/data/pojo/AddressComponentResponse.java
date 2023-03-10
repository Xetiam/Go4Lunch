package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressComponentResponse {
    @JsonProperty("long_name")
    String longName;
    @JsonProperty("short_name")
    String shortName;
    @JsonProperty("types")
    String types;
}
