package com.example.go4lunch.ui.restaurantmap;

import com.example.go4lunch.model.RestaurantEntity;

import java.util.List;

public class RestaurantMapViewState {
}
class WithResponseState extends RestaurantMapViewState{
    private final List<RestaurantEntity> restaurants;

    public WithResponseState(List<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
    }

    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }
}

