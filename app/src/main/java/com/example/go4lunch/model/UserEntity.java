package com.example.go4lunch.model;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserEntity {
    private String uid;
    private String username;
    @Nullable
    private Uri urlPicture;
    private String lunchChoice;
    private ArrayList<String> evaluations;

    public UserEntity(String uid,
                      String username,
                      @Nullable Uri urlPicture,
                      String lunchChoice,
                      ArrayList<String> evaluations) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.lunchChoice = lunchChoice;
        this.evaluations = evaluations;
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
    public Uri getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable Uri urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getLunchChoice() {
        return lunchChoice;
    }

    public void setLunchChoice(String lunchChoice) {
        this.lunchChoice = lunchChoice;
    }
    public ArrayList<String> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(ArrayList<String> evaluations) {
        this.evaluations = evaluations;
    }
}
