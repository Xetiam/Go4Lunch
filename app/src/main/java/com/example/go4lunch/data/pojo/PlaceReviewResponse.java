package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceReviewResponse {
    @SerializedName("author_name")
    String authorName;
    @SerializedName("rating")
    int rating;
    @SerializedName("relative_time_description")
    String relativeTimeDescription;
    @SerializedName("time")
    int time;
    @SerializedName("author_url")
    String authorUrl;
    @SerializedName("language")
    String language;
    @SerializedName("original_language")
    String originalLanguage;
    @SerializedName("profile_photo_url")
    String profilePhotoUrl;
    @SerializedName("text")
    String text;
    @SerializedName("translated")
    Boolean translated;
}
