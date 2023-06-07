package com.example.go4lunch.data.user;

import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.SignOutCallback;

import java.util.ArrayList;

public interface UserRepositoryContract {
    UserEntity getCurrentUser();

    String getCurrentUserUID();

    void createUser();

    void updateLunchChoiceOnUserAndPreviousRestaurant(String restaurantId, DetailCallback callback);

    void deleteUserFromFirestore(SignOutCallback callback);

    void getCurrentUserEvaluations(DetailCallback callback, String restaurantId);

    void getUsersLuncherByIds(ArrayList<String> lunchers, DetailCallback callback);

    void getAllUserAndRestaurants(CoworkerCallback callback);

    void updateUsername(String newName);
}