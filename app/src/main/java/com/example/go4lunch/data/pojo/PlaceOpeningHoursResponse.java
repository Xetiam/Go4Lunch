package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PlaceOpeningHoursResponse {
    @JsonProperty("open_now")
    @Expose
    Boolean openNow;
    @JsonProperty("periods")
    @Expose
    ArrayList<PlaceOpeningHoursPeriodResponse> periods;
    @JsonProperty("special_days")
    @Expose
    ArrayList<PlaceSpecialDayResponse> specialDays;
    @JsonProperty("types")
    @Expose
    ArrayList<String> types;
    @JsonProperty("weekday_text")
    @Expose
    ArrayList<String> weekdayText;
}
