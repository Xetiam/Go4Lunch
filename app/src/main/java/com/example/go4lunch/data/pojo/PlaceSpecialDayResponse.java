package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceSpecialDayResponse {
    @SerializedName("date")
    String date;
    @SerializedName("exceptional_hours")
    Boolean exceptionalHours;
}
