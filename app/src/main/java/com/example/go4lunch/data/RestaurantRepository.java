package com.example.go4lunch.data;


import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.RestaurantCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantRepository implements RestaurantRepositoryContract {
    private static final String COLLECTION_NAME = "restaurants";
    private static final String RESTAURANT_NAME_FIELD = "restaurantname";
    private static final String RESTAURANT_DESCRIPTION_FIELD = "restaurantdescription";
    private static final String RESTAURANT_POSITION_FIELD = "restaurantposition";
    private static final String RESTAURANT_EVALUATIONS_FIELD = "restaurantevaluations";
    private static final String RESTAURANT_LUNCHERS_FIELD = "restaurantluncher";
    private static final String RESTAURANT_OPENING_HOURS_FIELD = "restaurantopeninghours";
    private static final String RESTAURANT_PICTURE_URL = "restaurantpictureurl";
    private static final String RESTAURANT_ID = "restaurantid";
    private static volatile RestaurantRepository instance;

    public void getRestaurants(RestaurantCallback callback) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            List<RestaurantEntity> entities = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    RestaurantEntity restaurant = getRestaurant(document);
                    entities.add(restaurant);
                }
            }
            callback.restaurantsCallback(entities);
        }).addOnFailureListener(exception -> {
            //TODO:cas d'erreur
        });
    }
    @Override
    public void createRestaurants(ArrayList<RestaurantEntity> restaurantEntities) {
        if (restaurantEntities != null) {
            for (RestaurantEntity restaurant : restaurantEntities) {
                putRestaurant(restaurant);
            }
        }
    }

    @Override
    public void addOrSuppressALuncher(String restaurantId, String userId) {
        Map<String, Object> data = new HashMap<>();
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                ArrayList<String> lunchers = new ArrayList<>();
                if (document.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                    lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
                    if (lunchers != null && !lunchers.contains(userId)) {
                        lunchers.add(userId);
                        data.put(RESTAURANT_LUNCHERS_FIELD, lunchers);
                        reference.document(restaurantId).update(data);
                    } else {
                        lunchers.remove(userId);
                    }
                } else {
                    lunchers.add(userId);
                }
                data.put(RESTAURANT_LUNCHERS_FIELD, lunchers);
                reference.document(restaurantId).update(data);
            }
        });
    }

    @Override
    public void addOrRemoveAnEvaluation(String restaurantId, Boolean remove) {
        Map<String, Object> data = new HashMap<>();
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                Long currentEval = 0L;
                if (document.get(RESTAURANT_EVALUATIONS_FIELD) != null) {
                    currentEval = (Long) document.get(RESTAURANT_EVALUATIONS_FIELD);
                }
                if (remove) {
                    data.put(RESTAURANT_EVALUATIONS_FIELD, currentEval - 1L);
                } else {
                    data.put(RESTAURANT_EVALUATIONS_FIELD, currentEval + 1L);

                }
                reference.document(restaurantId).update(data);
            }
        });
    }

    private void putRestaurant(RestaurantEntity restaurant) {
        Map<String, Object> data = new HashMap<>();
        data.put(RESTAURANT_ID, restaurant.getRestaurantid());
        data.put(RESTAURANT_NAME_FIELD, restaurant.getRestaurantname());
        data.put(RESTAURANT_POSITION_FIELD, new GeoPoint(restaurant.getRestaurantposition().latitude, restaurant.getRestaurantposition().longitude));
        data.put(RESTAURANT_DESCRIPTION_FIELD, restaurant.getRestaurantdescription());
        data.put(RESTAURANT_OPENING_HOURS_FIELD, restaurant.getOpeningHour());
        data.put(RESTAURANT_PICTURE_URL, restaurant.getDrawableUrl());
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(restaurant.getRestaurantid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Map<String, Object> document = queryDocumentSnapshots.getData();
            if(document != null){
                reference.document(restaurant.getRestaurantid()).update(data);
            } else {
                reference.document(restaurant.getRestaurantid()).set(data);
            }
        });

    }

    private RestaurantEntity getRestaurant(Map<String, Object> document) {
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
}

