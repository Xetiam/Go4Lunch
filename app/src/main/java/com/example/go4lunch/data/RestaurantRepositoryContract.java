package com.example.go4lunch.data;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;

import java.util.ArrayList;

public interface RestaurantRepositoryContract {
    void getRestaurants(RestaurantCallback callback);
    void createRestaurants(ArrayList<RestaurantEntity> restaurantEntities);
    void addOrRemoveAnEvaluationOnRestaurantAndUser(String restaurantId, String userId, Boolean remove, DetailCallback callback);
    void getLunchers(String restaurantId,String userId, DetailCallback callback);
}
