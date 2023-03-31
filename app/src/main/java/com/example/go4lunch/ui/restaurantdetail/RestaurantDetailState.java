package com.example.go4lunch.ui.restaurantdetail;

import com.example.go4lunch.model.RestaurantDetailEntity;

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