package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceEditorialSummaryResponse {
    @JsonProperty("language")
    String language;
    @JsonProperty("overview")
    String overview;

}
