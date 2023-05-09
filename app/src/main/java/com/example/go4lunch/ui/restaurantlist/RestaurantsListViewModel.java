package com.example.go4lunch.ui.restaurantlist;

import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.RestaurantRepositoryContract;
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

    public RestaurantsListViewModel(RestaurantRepositoryContract restaurantRepository) {
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
        double distance;
        double distanceTemp;
        int indexToAdd;
        List<RestaurantEntity> sortedRestaurants = new ArrayList<>();
        for (RestaurantEntity restaurant : entities) {
            distance = computeDistanceBetween(
                    userPosition,
                    new LatLng(restaurant.getRestaurantposition().latitude,
                            restaurant.getRestaurantposition().longitude));
            if (distance <= 20000) {
                switch (sortedRestaurants.size()) {
                    case 0: {
                        sortedRestaurants.add(restaurant);
                        break;
                    }
                    case 1: {
                        distanceTemp = computeDistanceBetween(
                                userPosition,
                                new LatLng(sortedRestaurants.get(0).getRestaurantposition().latitude,
                                        sortedRestaurants.get(0).getRestaurantposition().longitude));
                        if (distance > distanceTemp) {
                            sortedRestaurants.add(restaurant);
                        } else {
                            sortedRestaurants.add(0, restaurant);
                        }
                        break;
                    }
                    default: {
                        indexToAdd = -1;
                        for (int i = 0; i < sortedRestaurants.size(); i++) {
                            distanceTemp = computeDistanceBetween(
                                    userPosition,
                                    new LatLng(sortedRestaurants.get(i).getRestaurantposition().latitude,
                                            sortedRestaurants.get(i).getRestaurantposition().longitude));
                            if (distance > distanceTemp) {
                                indexToAdd = -1;

                            } else if (indexToAdd == -1) {
                                indexToAdd = i;
                            }
                        }
                        if (indexToAdd == -1) {
                            sortedRestaurants.add(restaurant);
                        } else {
                            sortedRestaurants.add(indexToAdd, restaurant);
                        }
                    }
                }
            }
        }
        return sortedRestaurants;
    }

    public void sortRestaurants(int i) {
        ArrayList<RestaurantEntity> arrayToSort = (ArrayList<RestaurantEntity>) fetchedRestaurants;
        switch (i) {
            case 0: {
                arrayToSort = (ArrayList<RestaurantEntity>) sortRestaurantsByPosition(fetchedRestaurants);
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
