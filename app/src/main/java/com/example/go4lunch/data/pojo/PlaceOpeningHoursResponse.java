package com.example.go4lunch.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceOpeningHoursResponse {
    @SerializedName("open_now")
    Boolean openNow;
    @SerializedName("weekday_text")
    ArrayList<String> weekdayText;
}
