package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class AddressComponentResponse {
    @SerializedName("long_name")
    String longName;
    @SerializedName("short_name")
    String shortName;
    @SerializedName("types")
    String types;
}
