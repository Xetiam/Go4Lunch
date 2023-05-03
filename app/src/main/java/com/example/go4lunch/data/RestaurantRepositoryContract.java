package com.example.go4lunch.data;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.RestaurantsCallback;

import java.util.ArrayList;

public interface RestaurantRepositoryContract {
    void getRestaurants(RestaurantsCallback callback);

    void createRestaurants(ArrayList<RestaurantEntity> restaurantEntities);

    void addOrRemoveAnEvaluationOnRestaurantAndUser(String restaurantId, String userId, Boolean remove, DetailCallback callback);

    void getLunchers(String restaurantId, String userId, DetailCallback callback);

    void getRestaurantById(String lunchChoice, RestaurantCallback callback);
}
