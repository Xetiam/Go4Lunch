package com.example.go4lunch.ui.restaurantlist;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.RestaurantRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.RestaurantCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantListViewModel extends ViewModel implements RestaurantCallback {
    private final RestaurantRepositoryContract restaurantRepository;
    private final MutableLiveData<RestaurantListViewState> _state = new MutableLiveData<>();
    final LiveData<RestaurantListViewState> state = _state;
    private List<RestaurantEntity> fetchedRestaurants = new ArrayList<>();
    private LatLng userPosition;

    public RestaurantListViewModel(RestaurantRepositoryContract restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void initRestaurantList(LatLng userPosition) {
        this.userPosition = userPosition;
        restaurantRepository.getRestaurants(this);
    }

    public void search(String s) {
        List<RestaurantEntity> restaurantsFiltered = new ArrayList<>();
        for (RestaurantEntity restaurant : fetchedRestaurants) {
            if(!Objects.equals(s, "")) {
                if (restaurant.getRestaurantname().toLowerCase().contains(s.toLowerCase())) {
                    restaurantsFiltered.add(restaurant);
                }
            } else {
                restaurantsFiltered = fetchedRestaurants;
            }
        }
        _state.setValue(new WithResponseState(restaurantsFiltered));
    }

    @Override
    public void restaurantsCallback(List<RestaurantEntity> entities) {
        double distance;
        double distanceTemp;
        int indexToAdd;
        List<RestaurantEntity> filteredRestaurants = new ArrayList<>();
        for (RestaurantEntity restaurant : entities) {
            distance = computeDistanceBetween(
                    userPosition,
                    new LatLng(restaurant.getRestaurantposition().latitude,
                            restaurant.getRestaurantposition().longitude));
            if (distance <= 20000) {
                switch (filteredRestaurants.size()) {
                    case 0: {
                        filteredRestaurants.add(restaurant);
                        break;
                    }
                    case 1: {
                        distanceTemp = computeDistanceBetween(
                                userPosition,
                                new LatLng(filteredRestaurants.get(0).getRestaurantposition().latitude,
                                        filteredRestaurants.get(0).getRestaurantposition().longitude));
                        if(distance > distanceTemp){
                            filteredRestaurants.add(restaurant);
                        } else {
                            filteredRestaurants.add(0,restaurant);
                        }
                        break;
                    }
                    default: {
                        indexToAdd = -1;
                        for(int i = 0; i < filteredRestaurants.size(); i++){
                            distanceTemp = computeDistanceBetween(
                                    userPosition,
                                    new LatLng(filteredRestaurants.get(i).getRestaurantposition().latitude,
                                            filteredRestaurants.get(i).getRestaurantposition().longitude));
                            if(distance > distanceTemp){
                                indexToAdd = -1;

                            } else if(indexToAdd == -1) {
                                indexToAdd = i;
                            }
                        }
                        if(indexToAdd == -1){
                            filteredRestaurants.add(restaurant);
                        } else {
                            filteredRestaurants.add(indexToAdd,restaurant);
                        }
                    }
                }
            }
        }
        fetchedRestaurants = filteredRestaurants;
        _state.setValue(new WithResponseState(filteredRestaurants));
    }
}
