package com.example.go4lunch.ui.restaurantmap;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMapViewModel extends ViewModel {

    private PlacesRepository placesRepository;
    private LiveData<WithResponseState> responseLiveData;

    public RestaurantMapViewModel(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
        responseLiveData = new MutableLiveData<>();
    }


    public LiveData<WithResponseState> getResponseLiveData(LatLng userPosition) {
        return responseLiveData = Transformations.map(placesRepository.getPlacesLiveData(userPosition),
                restaurants ->
                        mapDataToViewState(restaurants,userPosition)

        );
    }

    private WithResponseState mapDataToViewState(List<RestaurantEntity> restaurants, LatLng userPosition) {
        List<RestaurantEntity> fetchedRestaurants = new ArrayList<>();
        if(restaurants != null) {
            fetchedRestaurants = restaurants;
        }
        return new WithResponseState(fetchedRestaurants);
    }
}
