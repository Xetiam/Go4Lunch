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
            List<DocumentSnapshot> test = queryDocumentSnapshots.getDocuments();
            List<String> ids = new ArrayList<>();
            List<Long> evals = new ArrayList<>();
            List<String> descriptions = new ArrayList<>();
            List<String> names = new ArrayList<>();
            List<String> pictures = new ArrayList<>();
            List<GeoPoint> positions = new ArrayList<>();
            List<String> openingHours = new ArrayList<>();
            List<RestaurantEntity> entities = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : test) {
                if (documentSnapshot != null) {
                    for (Map.Entry<String, Object> entry : Objects.requireNonNull(documentSnapshot.getData()).entrySet()) {
                        switch(entry.getKey()){//TODO : switch fonctionne pas alors que if/elseif oui
                            case "restaurantid" : ids.add((String) entry.getValue());
                            case "evaluation" : evals.add((Long) entry.getValue());
                            case "restaurantdescription" : descriptions.add((String) entry.getValue());
                            case "restaurantname" : names.add((String) entry.getValue());
                            case "restaurantposition" : positions.add((GeoPoint) entry.getValue());
                            case "restaurantopening" : openingHours.add((String) entry.getValue());
                            case "restaurantpicture" : pictures.add((String) entry.getValue());
                        }
                    }
                }
                for (int i = 0; i < ids.size(); i++) {
                    entities.add(new RestaurantEntity(
                            ids.get(i),
                            names.get(i),
                            descriptions.get(i),
                            openingHours.get(i),
                            positions.get(i),
                            evals.get(i),
                            pictures.get(i))
                    );
                }
            }
            callback.restaurantsCallback(entities);
        }).addOnFailureListener(exception -> {
            //TODO:cas d'erreur
        });
    }

    public void createRestaurant(String name,
                                 String description,
                                 LatLng position,
                                 List<Integer> evaluations,
                                 String id) {
        Map<String, Object> data1 = new HashMap<>();
        data1.put(RESTAURANT_ID, id);
        data1.put(RESTAURANT_DESCRIPTION_FIELD, description);
        data1.put(RESTAURANT_EVALUATIONS_FIELDS, evaluations);
        data1.put(RESTAURANT_POSITION_FIELD, position);
        data1.put(RESTAURANT_NAME_FIELD, name);

        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        reference.document(id).set(data1);
    }
}

