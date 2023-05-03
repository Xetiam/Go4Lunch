package com.example.go4lunch.ui.restaurantlist;

import com.example.go4lunch.model.RestaurantEntity;

import java.util.List;

public class RestaurantListViewState {
}

class WithResponseState extends RestaurantListViewState {
    private final List<RestaurantEntity> restaurants;

    public WithResponseState(List<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
    }

    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }
}
