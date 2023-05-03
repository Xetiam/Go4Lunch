package com.example.go4lunch.ui;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;

public class RestaurantState {
}

class UserDrawerState extends RestaurantState {
    private final UserEntity user;
    private final String userMail;

    public UserDrawerState(UserEntity user, String currentUserMail) {
        this.user = user;
        this.userMail = currentUserMail;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getUserMail() {
        return userMail;
    }
}

class LunchChoiceState extends RestaurantState {
    private final RestaurantEntity lunchChoice;

    LunchChoiceState(RestaurantEntity lunchChoice) {
        this.lunchChoice = lunchChoice;
    }

    public RestaurantEntity getLunchChoice() {
        return lunchChoice;
    }
}
class NoLunchChoiceState extends RestaurantState {
}