package com.example.go4lunch.utils;

import com.example.go4lunch.model.RestaurantEntity;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantCallback {
    void restaurantsCallback(List<RestaurantEntity> entities);
}
