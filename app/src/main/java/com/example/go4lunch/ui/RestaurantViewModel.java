package com.example.go4lunch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepositoryContract;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.RestaurantCallback;

public class RestaurantViewModel extends ViewModel implements RestaurantCallback {
    private final MutableLiveData<RestaurantState> _state = new MutableLiveData<>();
    private final UserRepository userRepository;
    private final RestaurantRepositoryContract restaurantRepository;
    LiveData<RestaurantState> state = _state;

    public RestaurantViewModel(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void setUserDrawer() {
        _state.setValue(new UserDrawerState(userRepository.getCurrentUser(), userRepository.getCurrentUserMail()));
    }

    public void onMyLunchClick() {
        userRepository.getCurrentUserLunch(this);
    }

    @Override
    public void restaurantCallback(RestaurantEntity entity) {
        _state.setValue(new LunchChoiceState(entity));
    }

    @Override
    public void userCallback(String lunchChoice) {
        if (lunchChoice != null && !lunchChoice.equals("")) {
            restaurantRepository.getRestaurantById(lunchChoice, this);
        } else {
            _state.setValue(new NoLunchChoiceState());
        }
    }
}
