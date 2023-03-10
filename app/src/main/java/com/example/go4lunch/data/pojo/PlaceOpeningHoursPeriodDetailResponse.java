package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class PlaceOpeningHoursPeriodDetailResponse {
    @JsonProperty("day")
    @Expose
    int day;
    @JsonProperty("time")
    @Expose
    String time;
    @JsonProperty("date")
    @Expose
    String date;
    @JsonProperty("truncated")
    @Expose
    Boolean truncated;

}
