package com.example.go4lunch.ui.restaurantdetail;

import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.ArrayList;

public class RestaurantDetailState {
}
class WithResponseState extends RestaurantDetailState{
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

    public ArrayList<UserEntity> getUserEntities() {
        return userEntities;
    }

    private final ArrayList<UserEntity> userEntities;

    LuncherState(ArrayList<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }
}

class CurrentUserLunchState extends RestaurantDetailState {
    CurrentUserLunchState(boolean isCurrentUserLuncher) {
        this.isCurrentUserLuncher = isCurrentUserLuncher;
    }

    public boolean isCurrentUserLuncher() {
        return isCurrentUserLuncher;
    }
    private final boolean isCurrentUserLuncher;


}