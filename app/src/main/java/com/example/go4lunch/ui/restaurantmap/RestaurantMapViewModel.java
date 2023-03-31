package com.example.go4lunch.ui.restaurantmap;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.RestaurantRepository;
import com.example.go4lunch.data.RestaurantRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMapViewModel extends ViewModel {

    private final PlacesRepository placesRepository;
    private final RestaurantRepositoryContract restaurantRepository;
    private final MutableLiveData<RestaurantMapViewState> _state = new MutableLiveData<>();
    LiveData<RestaurantMapViewState> state = _state;

    public RestaurantMapViewModel(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
        this.restaurantRepository = new RestaurantRepository();
    }


    public void getResponseLiveData(LatLng userPosition) {
        LiveData<WithResponseState> responseLiveData = Transformations.map(placesRepository.getPlacesLiveData(userPosition),
                this::mapDataToViewState
        );
        responseLiveData.observeForever(this::setStateOnResponse);
    }

    private void setStateOnResponse(WithResponseState withResponseState) {
        _state.setValue(withResponseState);
    }

    private WithResponseState mapDataToViewState(List<RestaurantEntity> restaurants) {
        ArrayList<RestaurantEntity> fetchedRestaurants = new ArrayList<>();
        if (restaurants != null) {
            fetchedRestaurants = (ArrayList<RestaurantEntity>) restaurants;
        }
        restaurantRepository.createRestaurants(fetchedRestaurants);
        return new WithResponseState(restaurants);
    }
}
