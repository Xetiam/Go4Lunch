package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PlacePhotoResponse {
    @JsonProperty("height")
    @Expose
    int height;
    @JsonProperty("html_attributions")
    @Expose
    ArrayList<String> htmlAttributions;
    @JsonProperty("photo_reference")
    @Expose
    String photoReference;
    @JsonProperty("width")
    @Expose
    int width;
}
