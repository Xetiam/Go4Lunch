package com.example.go4lunch.model;

import androidx.annotation.Nullable;

public class UserEntity {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String lunchChoice;

    public UserEntity(String uid,
                      String username,
                      @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.lunchChoice = "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getLunchChoice() {
        return lunchChoice;
    }

    public void setLunchChoice(String lunchChoice) {
        this.lunchChoice = lunchChoice;
    }
}
