package com.example.go4lunch.data.callback;

import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.RestaurantsCallback;

import java.util.ArrayList;
import java.util.Map;

public interface CallbackRestaurantDataSource {
    void restaurantsData(ArrayList<Map<String, Object>> restaurantDocuments, RestaurantsCallback callback);

    void lunchersData(Map<String, Object> restaurantDocument, DetailCallback callback, String userId);

    void restaurantData(Map<String, Object> restaurantDocument, RestaurantCallback callback);

    void addOrRemoveEvaluationData(Map<String, Object> restaurantDocument, Map<String, Object> userDocument, DetailCallback callback, Boolean remove);
}
