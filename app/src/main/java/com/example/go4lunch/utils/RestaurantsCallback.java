package com.example.go4lunch.utils;

import com.example.go4lunch.model.RestaurantEntity;

import java.util.List;

public interface RestaurantsCallback {
    void restaurantsCallback(List<RestaurantEntity> entities);
}
