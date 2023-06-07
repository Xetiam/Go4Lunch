package com.example.go4lunch.data.restaurant;

import static com.example.go4lunch.data.restaurant.RestaurantRepository.RESTAURANT_EVALUATIONS_FIELD;
import static com.example.go4lunch.data.user.UserRepository.USER_ID_FIELD;

import com.example.go4lunch.data.callback.CallbackRestaurantDataSource;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.RestaurantsCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestaurantDataSource {
    public static final String RESTAURANT_COLLECTION_NAME = "restaurants";
    public static final String USER_COLLECTION_NAME = "users";
    public static final String RESTAURANT_ID = "restaurantid";


    private final FirebaseFirestore reference = FirebaseFirestore.getInstance();

    //Restaurant
    public void getRestaurantsData(CallbackRestaurantDataSource callbackDataSource, RestaurantsCallback callback) {
        reference.collection(RESTAURANT_COLLECTION_NAME).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            ArrayList<Map<String, Object>> restaurantDocuments = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documents) {
                Map<String, Object> document = documentSnapshot.getData();
                if (document != null) {
                    restaurantDocuments.add(document);
                }
            }
            callbackDataSource.restaurantsData(restaurantDocuments, callback);
        });
    }

    public void getLunchersData(String restaurantId, String userId, CallbackRestaurantDataSource callbackDataSource, DetailCallback callback) {
        reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> document = documentSnapshot.getData();
            if (document != null) {
                callbackDataSource.lunchersData(document, callback, userId);
            }
        });
    }

    public void getRestaurantByIdData(String lunchChoice, CallbackRestaurantDataSource callbackDataSource, RestaurantCallback callback) {
        reference.collection(RESTAURANT_COLLECTION_NAME).document(lunchChoice).get().addOnSuccessListener(documentSnapshot ->
                callbackDataSource.restaurantData(documentSnapshot.getData(), callback));
    }

    public void addOrRemoveAnEvaluationOnRestaurantAndUserData(String restaurantId, String userId, Boolean remove, CallbackRestaurantDataSource callbackRestaurantDataSource, DetailCallback callback) {
        reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).get().addOnSuccessListener(restaurantDocumentSnapshot -> {
            Map<String, Object> restaurantDocument = restaurantDocumentSnapshot.getData();
            reference.collection(USER_COLLECTION_NAME).document(userId).get().addOnSuccessListener(userDocumentSnapshot -> {
                Map<String, Object> userDocument = userDocumentSnapshot.getData();
                callbackRestaurantDataSource.addOrRemoveEvaluationData(restaurantDocument, userDocument, callback, remove);
            });
        });
    }

    public void updateDocuments(Map<String, Object> restaurantDocument, Map<String, Object> userDocument, DetailCallback callback, Boolean isEvaluate) {
        WriteBatch batch = reference.batch();
        String restaurantId = (String) restaurantDocument.get(RESTAURANT_ID);
        String userId = (String) userDocument.get(USER_ID_FIELD);

        reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).get().addOnSuccessListener(restaurantDocumentSnapshot -> {
            Map<String, Object> oldRestaurantDocument = restaurantDocumentSnapshot.getData();
            if (oldRestaurantDocument.get(RESTAURANT_EVALUATIONS_FIELD) != null) {
                batch.update(reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId), restaurantDocument);
            } else {
                batch.set(reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId), restaurantDocument);
            }
            reference.collection(USER_COLLECTION_NAME).document(userId).get().addOnSuccessListener(userDocumentSnapshot -> {
                Map<String, Object> oldUserDocument = userDocumentSnapshot.getData();
                if (oldUserDocument.get(USER_ID_FIELD) != null) {
                    batch.update(reference.collection(USER_COLLECTION_NAME).document(userId), userDocument);
                } else {
                    batch.set(reference.collection(RESTAURANT_COLLECTION_NAME).document(userId), restaurantDocument);
                }
                batch.commit().addOnCompleteListener(task -> callback.evaluationsCallback(isEvaluate));
            });
        });
    }

    public void putRestaurantData(Map<String, Object> data, String restaurantId) {
        reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            Map<String, Object> document = queryDocumentSnapshots.getData();
            if (document != null) {
                reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).update(data);
            } else {
                reference.collection(RESTAURANT_COLLECTION_NAME).document(restaurantId).set(data);
            }
        });
    }
}
