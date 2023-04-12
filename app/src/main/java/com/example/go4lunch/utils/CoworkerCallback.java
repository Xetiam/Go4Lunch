package com.example.go4lunch.utils;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.ArrayList;

public interface CoworkerCallback {
    void coworkerCallback(ArrayList<UserEntity> users, ArrayList<RestaurantEntity> restaurantEntities);
}
