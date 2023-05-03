package com.example.go4lunch.utils;

import com.example.go4lunch.model.UserEntity;

import java.util.ArrayList;

public interface DetailCallback {
    void lunchersCallback(ArrayList<String> lunchers);

    void userCallback(ArrayList<UserEntity> users);

    void evaluationsCallback(boolean isEvaluate);

    void isLuncherCallback(boolean isLuncher);

}
