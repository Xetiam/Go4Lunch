package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceOpeningHoursResponse {
    @SerializedName("open_now")
    Boolean openNow;
    @SerializedName("periods")
    ArrayList<PlaceOpeningHoursPeriodResponse> periods;
    @SerializedName("special_days")
    ArrayList<PlaceSpecialDayResponse> specialDays;
    @SerializedName("types")
    ArrayList<String> types;
    @SerializedName("weekday_text")
    ArrayList<String> weekdayText;
}
