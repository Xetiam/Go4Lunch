package com.example.go4lunch.ui.restaurantlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.FirestoreCallback;
import com.example.go4lunch.data.RestaurantRepository;
import com.example.go4lunch.data.RestaurantRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RestaurantListViewModel extends ViewModel {
    private final MutableLiveData<RestaurantListViewState> _state = new MutableLiveData<>();
    final LiveData<RestaurantListViewState> state = _state;

    public void initRestaurantList() {
        RestaurantRepositoryContract repository = RestaurantRepository.getInstance();
        repository.getRestaurantsTask(
                (List<RestaurantEntity> entities) ->
                        _state.setValue(new WithResponseState(entities)));
    }
}
