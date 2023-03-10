package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class PlaceReviewResponse {
    @JsonProperty("author_name")
    @Expose
    String authorName;
    @JsonProperty("rating")
    @Expose
    int rating;
    @JsonProperty("relative_time_description")
    @Expose
    String relativeTimeDescription;
    @JsonProperty("time")
    @Expose
    int time;
    @JsonProperty("author_url")
    @Expose
    String authorUrl;
    @JsonProperty("language")
    @Expose
    String language;
    @JsonProperty("original_language")
    @Expose
    String originalLanguage;
    @JsonProperty("profile_photo_url")
    @Expose
    String profilePhotoUrl;
    @JsonProperty("text")
    @Expose
    String text;
    @JsonProperty("translated")
    @Expose
    Boolean translated;
}
