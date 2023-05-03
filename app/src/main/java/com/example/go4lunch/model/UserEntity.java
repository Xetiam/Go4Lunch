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

    public String getUsername() {
        return username;
    }

    @Nullable
    public Uri getUrlPicture() {
        return urlPicture;
    }

    public String getLunchChoice() {
        return lunchChoice;
    }

}
