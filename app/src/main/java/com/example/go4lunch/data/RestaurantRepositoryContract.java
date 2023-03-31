package com.example.go4lunch.data;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.RestaurantCallback;

import java.util.ArrayList;

public interface RestaurantRepositoryContract {
    void getRestaurants(RestaurantCallback callback);
    void createRestaurants(ArrayList<RestaurantEntity> restaurantEntities);
    void addOrSuppressALuncher(String restaurantId, String userId);
    void addOrRemoveAnEvaluation(String restaurantId, Boolean remove);
}
