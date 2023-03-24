package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceOpeningHoursPeriodResponse {
    @SerializedName("open")
    PlaceOpeningHoursPeriodDetailResponse open;
    @SerializedName("close")
    PlaceOpeningHoursPeriodDetailResponse close;
}
