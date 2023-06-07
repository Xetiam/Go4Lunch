package com.example.go4lunch.ui.restaurantlist;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.RestaurantsCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RestaurantsListViewModel extends ViewModel implements RestaurantsCallback {
    private final RestaurantRepositoryContract restaurantRepository;
    private final MutableLiveData<RestaurantListViewState> _state = new MutableLiveData<>();
    final LiveData<RestaurantListViewState> state = _state;
    private List<RestaurantEntity> fetchedRestaurants = new ArrayList<>();
    private LatLng userPosition;
    private String searchQuery;

    public RestaurantsListViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void initRestaurantList(LatLng userPosition) {
        this.userPosition = userPosition;
        restaurantRepository.getRestaurants(this);
    }

    public void search(String s) {
        searchQuery = s;
        _state.setValue(new WithResponseState(filterRestaurant(searchQuery, fetchedRestaurants)));
    }

    private List<RestaurantEntity> filterRestaurant(String searchQuery, List<RestaurantEntity> restaurantEntities) {
        List<RestaurantEntity> restaurantsFiltered = new ArrayList<>();
        if (!Objects.equals(searchQuery, "")) {
            for (RestaurantEntity restaurant : restaurantEntities) {
                if (restaurant.getRestaurantname().toLowerCase().contains(searchQuery.toLowerCase())) {
                    restaurantsFiltered.add(restaurant);
                }
            }
            return restaurantsFiltered;
        } else {
            return restaurantEntities;
        }
    }

    private List<RestaurantEntity> sortRestaurantsByPosition(List<RestaurantEntity> entities) {
        List<RestaurantEntity> sortedRestaurants = new ArrayList<>(entities);
        sortedRestaurants.removeIf(restaurant -> computeDistanceBetween(userPosition, restaurant.getRestaurantposition()) > 20000);
        sortedRestaurants.sort(Comparator.comparingDouble(restaurant -> computeDistanceBetween(userPosition, restaurant.getRestaurantposition())));
        return sortedRestaurants;
    }

    public void sortRestaurants(int i) {
        ArrayList<RestaurantEntity> arrayToSort = (ArrayList<RestaurantEntity>) fetchedRestaurants;
        switch (i) {
            case 0: {
                arrayToSort = (ArrayList<RestaurantEntity>) sortRestaurantsByPosition(fetchedRestaurants);
                break;
            }
            case 1: {
                arrayToSort.sort(Comparator.comparingInt(entity -> -Math.toIntExact(entity.getEvaluation())));
                break;
            }
            case 2: {
                arrayToSort.sort(Comparator.comparingInt(entity -> -entity.getLunchers().size()));
                break;
            }
        }
        if (!Objects.equals(searchQuery, "") && searchQuery != null) {
            _state.postValue(new WithResponseState(filterRestaurant(searchQuery, arrayToSort)));
        } else {
            _state.postValue(new WithResponseState(arrayToSort));
        }
    }

    @Override
    public void restaurantsCallback(List<RestaurantEntity> entities) {
        fetchedRestaurants = sortRestaurantsByPosition(entities);
        _state.setValue(new WithResponseState(fetchedRestaurants));
    }
}
