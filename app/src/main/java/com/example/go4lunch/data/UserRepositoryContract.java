package com.example.go4lunch.data;

import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;

import java.util.ArrayList;

public interface UserRepositoryContract {
    UserEntity getCurrentUser();
    String getCurrentUserUID();
    void createUser();
    void updateLunchChoiceOnUserAndPreviousRestaurant(String restaurantId, DetailCallback callback);
    void deleteUserFromFirestore();
    void getCurrentUserEvaluations(DetailCallback callback, String restaurantId);
    void getUsersLuncherByIds(ArrayList<String> lunchers, DetailCallback callback);

    void getAllUserAndRestaurants(CoworkerCallback callback);
}