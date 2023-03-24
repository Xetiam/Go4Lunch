package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlacePhotoResponse {
    @SerializedName("height")
    int height;
    @SerializedName("html_attributions")
    ArrayList<String> htmlAttributions;
    @SerializedName("photo_reference")
    String photoReference;
    @SerializedName("width")
    int width;
}
