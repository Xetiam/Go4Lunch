package com.example.go4lunch.data;

import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantRepositoryContract {
    void getRestaurantsTask(FirestoreCallback callback);
    void createRestaurant(ArrayList<RestaurantEntity> restaurantEntities);

}
