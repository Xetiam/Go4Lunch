package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class PlaceEditorialSummaryResponse {
    @SerializedName("language")
    String language;
    @SerializedName("overview")
    String overview;

}
