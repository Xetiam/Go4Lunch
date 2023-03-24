package com.example.go4lunch.data;


import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RestaurantRepository implements RestaurantRepositoryContract {
    private static final String COLLECTION_NAME = "restaurants";
    private static final String RESTAURANT_NAME_FIELD = "restaurantname";
    private static final String RESTAURANT_DESCRIPTION_FIELD = "restaurantdescription";
    private static final String RESTAURANT_POSITION_FIELD = "restaurantposition";
    private static final String RESTAURANT_EVALUATIONS_FIELDS = "restaurantevaluations";
    private static final String RESTAURANT_OPENING_HOURS_FIELDS = "restaurantopeninghours";
    private static final String RESTAURANT_PICTURE_URL = "restaurantpictureurl";
    private static final String RESTAURANT_ID = "restaurantid";
    private static volatile RestaurantRepository instance;

    public static RestaurantRepository getInstance() {
        RestaurantRepository result = instance;
        if (result == null) {
            instance = new RestaurantRepository();
        }
        return instance;
    }

    public void getRestaurantsTask(FirestoreCallback callback) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            List<RestaurantEntity> entities = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String,Object> document = documentSnapshot.getData();
                if (document != null) {
                    String id = (String) document.get(RESTAURANT_ID);
                    Long eval = (Long) document.get(RESTAURANT_EVALUATIONS_FIELDS);
                    String description = (String) document.get(RESTAURANT_DESCRIPTION_FIELD);
                    String name = (String) document.get(RESTAURANT_NAME_FIELD);
                    GeoPoint position = (GeoPoint) document.get(RESTAURANT_POSITION_FIELD);
                    String openingHour = (String) document.get(RESTAURANT_OPENING_HOURS_FIELDS);
                    String picture = (String) document.get(RESTAURANT_PICTURE_URL);
                    entities.add(new RestaurantEntity(id,name,description,openingHour,position,eval,picture));
                }
            }
            callback.restaurantsCallback(entities);
        }).addOnFailureListener(exception -> {
            //TODO:cas d'erreur
        });
    }

    public void createRestaurant(ArrayList<RestaurantEntity> restaurantEntities) {
        Map<String, Object> data1 = new HashMap<>();
        for(RestaurantEntity restaurant: restaurantEntities){
            data1.put(RESTAURANT_ID, restaurant.getRestaurantid());
            data1.put(RESTAURANT_NAME_FIELD, restaurant.getRestaurantname());
            data1.put(RESTAURANT_POSITION_FIELD, new GeoPoint(restaurant.getRestaurantposition().latitude, restaurant.getRestaurantposition().longitude));
            data1.put(RESTAURANT_DESCRIPTION_FIELD, restaurant.getRestaurantdescription());
            data1.put(RESTAURANT_EVALUATIONS_FIELDS, restaurant.getEvaluation());
            data1.put(RESTAURANT_OPENING_HOURS_FIELDS, restaurant.getOpeningHour());
            data1.put(RESTAURANT_PICTURE_URL, restaurant.getDrawableUrl());
            CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
            reference.document(restaurant.getRestaurantid()).set(data1);
        }
    }
}

