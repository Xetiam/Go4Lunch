package com.example.go4lunch.ui.coworker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.user.UserRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;

import java.util.ArrayList;
import java.util.Objects;

public class CoworkerViewModel extends ViewModel implements CoworkerCallback {
    private final UserRepositoryContract userRepository;
    private final MutableLiveData<CoworkerViewState> _state = new MutableLiveData<>();
    final LiveData<CoworkerViewState> state = _state;
    private ArrayList<UserEntity> fetchedUsers;
    private ArrayList<RestaurantEntity> fetchedRestaurants;

    public CoworkerViewModel(UserRepositoryContract userRepository) {
        this.userRepository = userRepository;
    }

    void initCoworker() {
        userRepository.getAllUserAndRestaurants(this);
    }

    @Override
    public void coworkerCallback(ArrayList<UserEntity> userEntities, ArrayList<RestaurantEntity> restaurantEntities) {
        fetchedUsers = userEntities;
        fetchedRestaurants = restaurantEntities;
        this._state.postValue(new UserResponseState(userEntities, restaurantEntities));
    }

    public void search(String s) {
        ArrayList<RestaurantEntity> restaurantsFiltered = new ArrayList<>();
        ArrayList<UserEntity> userFiltered = new ArrayList<>();
        if (!Objects.equals(s, "")) {
            for (RestaurantEntity restaurant : fetchedRestaurants) {
                if (restaurant.getRestaurantname().toLowerCase().contains(s.toLowerCase())) {
                    restaurantsFiltered.add(restaurant);
                    for (UserEntity user : fetchedUsers) {
                        if (Objects.equals(user.getLunchChoice(), restaurant.getRestaurantId())) {
                            userFiltered.add(user);
                        }
                    }
                }
            }
            this._state.postValue((new UserResponseState(userFiltered, restaurantsFiltered)));
        }
    }
}
