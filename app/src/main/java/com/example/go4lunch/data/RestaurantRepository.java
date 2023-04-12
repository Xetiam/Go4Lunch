package com.example.go4lunch.data;


import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;

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

    private static final String USER_COLLECTION_NAME = "users";
    private static final String USER_EVALUATION_FIELD = "evaluations";
    private final CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

    public void getRestaurants(RestaurantCallback callback) {
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
    public void addOrRemoveAnEvaluationOnRestaurantAndUser(String restaurantId, String userId, Boolean remove, DetailCallback callback) {
        FirebaseFirestore batchReference = FirebaseFirestore.getInstance();
        WriteBatch batch = batchReference.batch();
        reference.document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                Long currentEval;
                if (document.get(RESTAURANT_EVALUATIONS_FIELD) != null) {
                    currentEval = (Long) document.get(RESTAURANT_EVALUATIONS_FIELD);
                    if (remove) {
                        document.put(RESTAURANT_EVALUATIONS_FIELD, currentEval - 1L);
                    } else {
                        document.put(RESTAURANT_EVALUATIONS_FIELD, currentEval + 1L);
                    }
                    DocumentReference restaurantRef = batchReference.collection(COLLECTION_NAME).document(restaurantId);
                    batch.update(restaurantRef, document);
                } else {
                    document.put(RESTAURANT_EVALUATIONS_FIELD, 1L);
                    DocumentReference restaurantRef = batchReference.collection(COLLECTION_NAME).document(restaurantId);
                    batch.set(restaurantRef, document);
                }
                addOrRemoveAnEvaluationOnUser(restaurantId,userId,callback,batch);

            }
        });
    }

    private void addOrRemoveAnEvaluationOnUser(String restaurantId, String userId, DetailCallback callback, WriteBatch batch) {
        FirebaseFirestore batchReference = FirebaseFirestore.getInstance();
        batchReference.collection(USER_COLLECTION_NAME).document(userId).get().addOnSuccessListener(userDocumentSnapshot -> {
            DocumentReference userRef = batchReference.collection(USER_COLLECTION_NAME).document(userId);
            Map<String, Object> userDocument = userDocumentSnapshot.getData();
            ArrayList<String> evaluations = new ArrayList<>();
            boolean hasEvaluate = false;
            if (userDocument != null) {
                if (userDocument.get(USER_EVALUATION_FIELD) != null) {
                    evaluations = (ArrayList<String>) userDocument.get(USER_EVALUATION_FIELD);
                    if (evaluations.contains(restaurantId)) {
                        evaluations.remove(restaurantId);
                    } else {
                        evaluations.add(restaurantId);
                        hasEvaluate = true;
                    }
                    userDocument.put(USER_EVALUATION_FIELD, evaluations);
                    batch.update(userRef, userDocument);
                } else {
                    evaluations.add(restaurantId);
                    userDocument.put(USER_EVALUATION_FIELD, evaluations);
                    batch.set(userRef, userDocument);
                }
                userDocument.put(USER_EVALUATION_FIELD, evaluations);
                batch.update(userRef, userDocument);
            }
            boolean finalHasEvaluate = hasEvaluate;
            batch.commit().addOnCompleteListener(task -> callback.evaluationsCallback(finalHasEvaluate));
        });
    }

    @Override
    public void getLunchers(String restaurantId,String userId, DetailCallback callback) {
        reference.document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null && document.get(RESTAURANT_LUNCHERS_FIELD) != null) {
                ArrayList<String> lunchers = (ArrayList<String>) document.get(RESTAURANT_LUNCHERS_FIELD);
                if(lunchers.contains(userId)){
                    lunchers.remove(userId);
                    callback.isLuncherCallback(true);
                }
                callback.lunchersCallback(lunchers);
            } else {
                callback.lunchersCallback(new ArrayList<>());
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
            if (document != null) {
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

