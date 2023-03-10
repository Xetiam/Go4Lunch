package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class PlaceSpecialDayResponse {
    @JsonProperty("date")
    @Expose
    String date;
    @JsonProperty("exceptional_hours")
    @Expose
    Boolean exceptionalHours;
}
