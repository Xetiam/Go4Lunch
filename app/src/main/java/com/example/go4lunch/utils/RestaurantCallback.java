package com.example.go4lunch.utils;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.List;

public interface RestaurantCallback {
    void restaurantCallback(RestaurantEntity entity);
    void userCallback(String lunchChoice);

}
