package com.example.go4lunch.ui.restaurantdetail;

import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.ArrayList;

public class RestaurantDetailState {
}

class WithResponseState extends RestaurantDetailState {
    private final RestaurantDetailEntity restaurantDetailEntity;

    public WithResponseState(RestaurantDetailEntity restaurantDetailEntity) {
        this.restaurantDetailEntity = restaurantDetailEntity;
    }

    public RestaurantDetailEntity getRestaurantDetailEntity() {
        return restaurantDetailEntity;
    }
}

class HasEvaluate extends RestaurantDetailState {

    private final boolean isEvaluate;

    public HasEvaluate(boolean isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public boolean isEvaluate() {
        return isEvaluate;
    }
}

class LuncherState extends RestaurantDetailState {

    private final ArrayList<UserEntity> userEntities;

    LuncherState(ArrayList<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public ArrayList<UserEntity> getUserEntities() {
        return userEntities;
    }
}

class CurrentUserLunchState extends RestaurantDetailState {
    private final boolean isCurrentUserLuncher;

    public boolean isSetNotification() {
        return setNotification;
    }

    private final boolean setNotification;

    CurrentUserLunchState(boolean isCurrentUserLuncher, boolean setNotification) {
        this.isCurrentUserLuncher = isCurrentUserLuncher;
        this.setNotification = setNotification;
    }

    public boolean isCurrentUserLuncher() {
        return isCurrentUserLuncher;
    }


}