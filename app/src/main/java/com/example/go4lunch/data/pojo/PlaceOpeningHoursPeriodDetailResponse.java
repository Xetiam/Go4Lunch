package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOpeningHoursPeriodDetailResponse {
    @SerializedName("day")
    int day;
    @SerializedName("time")
    String time;
    @SerializedName("date")
    String date;
    @SerializedName("truncated")
    Boolean truncated;

}
