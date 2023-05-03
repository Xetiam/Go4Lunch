package com.example.go4lunch.ui.coworker;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.ArrayList;

public class CoworkerViewState {
}

class UserResponseState extends CoworkerViewState {
    private final ArrayList<UserEntity> userEntities;
    private final ArrayList<RestaurantEntity> restaurantEntities;

    public UserResponseState(ArrayList<UserEntity> users, ArrayList<RestaurantEntity> restaurantEntities) {
        this.userEntities = users;
        this.restaurantEntities = restaurantEntities;
    }

    public ArrayList<UserEntity> getUserEntities() {
        return userEntities;
    }

    public ArrayList<RestaurantEntity> getRestaurantEntities() {
        return restaurantEntities;
    }
}
