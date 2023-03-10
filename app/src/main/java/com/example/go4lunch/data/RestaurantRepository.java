package com.example.go4lunch.data;


import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.go4lunch.model.UserEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Task<QuerySnapshot> getRestaurantsTask(LatLng userPosition) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        return reference.get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                );
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

