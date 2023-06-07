package com.example.go4lunch.data.callback;

import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;

import java.util.ArrayList;
import java.util.Map;

public interface CallbackUserDataSource {
    void usersData(ArrayList<Map<String,Object>> userDocuments, DetailCallback callback);
    void usersAndRestaurantsData(ArrayList<Map<String,Object>> userDocuments, ArrayList<Map<String,Object>> restaurantDocuments, CoworkerCallback callback);
}
