package com.example.go4lunch.utils;

import com.example.go4lunch.model.RestaurantEntity;

public interface RestaurantCallback {
    void restaurantCallback(RestaurantEntity entity);

    void userCallback(String lunchChoice);

}
