package com.example.go4lunch.data;

import com.example.go4lunch.model.RestaurantEntity;

import java.util.List;

public interface FirestoreCallback {
    void restaurantsCallback(List<RestaurantEntity> entities);
}
