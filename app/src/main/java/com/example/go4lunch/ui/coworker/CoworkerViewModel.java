package com.example.go4lunch.ui.coworker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.UserRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;

import java.util.ArrayList;

public class CoworkerViewModel extends ViewModel implements CoworkerCallback {
    private final UserRepositoryContract userRepository;
    private final MutableLiveData<CoworkerViewState> _state = new MutableLiveData<>();
    final LiveData<CoworkerViewState> state = _state;

    public CoworkerViewModel(UserRepositoryContract userRepository) {
        this.userRepository = userRepository;
    }

    void initCoworker(){
        userRepository.getAllUserAndRestaurants(this);
    }

    @Override
    public void coworkerCallback(ArrayList<UserEntity> userEntities, ArrayList<RestaurantEntity> restaurantEntities) {
        this._state.postValue(new UserResponseState(userEntities, restaurantEntities));
    }
}
