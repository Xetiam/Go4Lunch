package com.example.go4lunch.data.restaurant;


import static com.example.go4lunch.data.restaurant.RestaurantDataSource.RESTAURANT_ID;

import com.example.go4lunch.data.callback.CallbackRestaurantDataSource;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.RestaurantsCallback;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantRepository implements RestaurantRepositoryContract, CallbackRestaurantDataSource {
    public static final String RESTAURANT_NAME_FIELD = "restaurantname";
    public static final String RESTAURANT_DESCRIPTION_FIELD = "restaurantdescription";
    public static final String RESTAURANT_POSITION_FIELD = "restaurantposition";
    public static final String RESTAURANT_EVALUATIONS_FIELD = "restaurantevaluations";
    public static final String RESTAURANT_LUNCHERS_FIELD = "restaurantluncher";
    public static final String RESTAURANT_OPENING_HOURS_FIELD = "restaurantopeninghours";
    public static final String RESTAURANT_PICTURE_URL = "restaurantpictureurl";
    public static final String USER_EVALUATION_FIELD = "evaluations";
    public RestaurantDataSource dataSource;

    public RestaurantRepository(RestaurantDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void getRestaurants(RestaurantsCallback callback) {
        dataSource.getRestaurantsData(this, callback);
    }

    @Override
    public void getLunchers(String restaurantId, String userId, DetailCallback callback) {
        dataSource.getLunchersData(restaurantId, userId, this, callback);
    }

    @Override
    public void getRestaurantById(String lunchChoice, RestaurantCallback callback) {
        dataSource.getRestaurantByIdData(lunchChoice, this, callback);
    }

    @Override
    public void createRestaurants(ArrayList<RestaurantEntity> restaurantEntities) {
        if (restaurantEntities != null) {
            for (RestaurantEntity restaurant : restaurantEntities) {
                putRestaurantFromEntity(restaurant);
            }
        }
    }

    @Override
    public void addOrRemoveAnEvaluationOnRestaurantAndUser(String restaurantId, String userId, Boolean remove, DetailCallback callback) {
        dataSource.addOrRemoveAnEvaluationOnRestaurantAndUserData(restaurantId, userId, remove, this, callback);
    }

    private void putRestaurantFromEntity(RestaurantEntity restaurant) {
        Map<String, Object> data = new HashMap<>();
        data.put(RESTAURANT_ID, restaurant.getRestaurantId());
        data.put(RESTAURANT_NAME_FIELD, restaurant.getRestaurantname());
        data.put(RESTAURANT_POSITION_FIELD, new GeoPoint(restaurant.getRestaurantposition().latitude, restaurant.getRestaurantposition().longitude));
        data.put(RESTAURANT_DESCRIPTION_FIELD, restaurant.getRestaurantdescription());
        data.put(RESTAURANT_OPENING_HOURS_FIELD, restaurant.getOpeningHour());
        data.put(RESTAURANT_PICTURE_URL, restaurant.getDrawableUrl());
        dataSource.putRestaurantData(data, restaurant.getRestaurantId());
    }

    public static RestaurantEntity getRestaurantFromDocument(Map<String, Object> document) {
        String id = "";
        Long eval = 0L;
        String description = "";
        String name = "";
        GeoPoint position = null;
        String openingHour = "";
        String picture = "";
        ArrayList<String> lunchers = new ArrayList<>();
        if (document != null) {
            id = (String) document.get(RESTAURANT_ID);
            eval = (Long) document.get(RESTAURANT_EVALUATIONS_FIELD);
            description = (String) document.get(RESTAURANT_DESCRIPTION_FIELD);
            name = (String) document.get(RESTAURANT_NAME_FIELD);
            position = (GeoPoint) document.get(RESTAURANT_POSITION_FIELD);
            openingHour = (String) document.get(RESTAURANT_OPENING_HOURS_FIELD);
            picture = (String) document.get(RESTAURANT_PICTURE_URL);
            lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
        }
        return new RestaurantEntity(id, name, description, openingHour, position, eval, picture, lunchers);
    }

    @Override
    public void restaurantsData(ArrayList<Map<String, Object>> restaurantDocuments, RestaurantsCallback callback) {
        ArrayList<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (Map<String, Object> document : restaurantDocuments) {
            restaurantEntities.add(getRestaurantFromDocument(document));
        }
        callback.restaurantsCallback(restaurantEntities);
    }

    @Override
    public void lunchersData(Map<String, Object> restaurantDocument, DetailCallback callback, String userId) {
        RestaurantEntity restaurant = getRestaurantFromDocument(restaurantDocument);
        ArrayList<String> lunchers = restaurant.getLunchers();
        if (lunchers.contains(userId)) {
            if (lunchers.contains(userId)) {
                lunchers.remove(userId);
                callback.isLuncherCallback(true);
            } else {
                callback.isLuncherCallback(false);
            }
        } else {
            callback.isLuncherCallback(false);
            callback.lunchersCallback(new ArrayList<>());
        }
        //callback.lunchersCallback(lunchers);
    }

    @Override
    public void restaurantData(Map<String, Object> restaurantDocument, RestaurantCallback callback) {
        RestaurantEntity restaurant = getRestaurantFromDocument(restaurantDocument);
        callback.restaurantCallback(restaurant);
    }

    @Override
    public void addOrRemoveEvaluationData(Map<String, Object> restaurantDocument, Map<String, Object> userDocument, DetailCallback callback, Boolean remove) {
        if (restaurantDocument != null && userDocument != null) {
            long currentEval;
            boolean isEvaluate;
            ArrayList<String> userEval = userDocument.get(USER_EVALUATION_FIELD) == null ? new ArrayList<>() : (ArrayList<String>) userDocument.get(USER_EVALUATION_FIELD);
            String restaurantId = (String) restaurantDocument.get(RESTAURANT_ID);
            if (restaurantDocument.get(RESTAURANT_EVALUATIONS_FIELD) != null) {
                currentEval = (Long) restaurantDocument.get(RESTAURANT_EVALUATIONS_FIELD);
                if (remove && userEval.contains(restaurantId)) {
                    restaurantDocument.put(RESTAURANT_EVALUATIONS_FIELD, currentEval - 1L);
                    userEval.remove(restaurantId);
                    isEvaluate = false;
                } else {
                    restaurantDocument.put(RESTAURANT_EVALUATIONS_FIELD, currentEval + 1L);
                    userEval.add(restaurantId);
                    isEvaluate = true;
                }
            } else {
                restaurantDocument.put(RESTAURANT_EVALUATIONS_FIELD, 1L);
                userEval.add(restaurantId);
                isEvaluate = true;
            }
            userDocument.put(USER_EVALUATION_FIELD, userEval);
            dataSource.updateDocuments(restaurantDocument, userDocument, callback, isEvaluate);
        }
    }
}

