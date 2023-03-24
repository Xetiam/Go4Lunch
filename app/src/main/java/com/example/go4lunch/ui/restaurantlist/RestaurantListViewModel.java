package com.example.go4lunch.ui.restaurantlist;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.adapter.RestaurantRecyclerViewAdapter;
import com.example.go4lunch.data.FirestoreCallback;
import com.example.go4lunch.data.RestaurantRepository;
import com.example.go4lunch.data.RestaurantRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    private final MutableLiveData<RestaurantListViewState> _state = new MutableLiveData<>();
    final LiveData<RestaurantListViewState> state = _state;

    public void initRestaurantList(LatLng userPosition) {
        RestaurantRepositoryContract repository = RestaurantRepository.getInstance();
        repository.getRestaurantsTask(
                (List<RestaurantEntity> entities) -> {
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
                    _state.setValue(new WithResponseState(filteredRestaurants));
                });
    }
}
