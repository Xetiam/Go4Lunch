package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PlaceOpeningHoursPeriodResponse {
    @JsonProperty("open")
    @Expose
    PlaceOpeningHoursPeriodDetailResponse open;
    @JsonProperty("close")
    @Expose
    PlaceOpeningHoursPeriodDetailResponse close;
}
