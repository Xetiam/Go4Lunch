package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.RestaurantRepository;
import com.example.go4lunch.data.RetrofitService;
import com.example.go4lunch.ui.coworker.CoworkerViewModel;
import com.example.go4lunch.ui.login.LoginViewModel;
import com.example.go4lunch.ui.restaurantlist.RestaurantListViewModel;
import com.example.go4lunch.ui.restaurantmap.RestaurantMapViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static final class FactoryHolder {
        static final ViewModelFactory factory = new ViewModelFactory();
    }

    public static ViewModelFactory getInstance() {
        return FactoryHolder.factory;
    }

    private ViewModelFactory() {
    }
    private final PlacesRepository placesRepository = new PlacesRepository(
            RetrofitService.getPlacesApi()
    );

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        }
        if (modelClass.isAssignableFrom(RestaurantMapViewModel.class)) {
            return (T) new RestaurantMapViewModel(placesRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantListViewModel.class)) {
            return (T) new RestaurantListViewModel();
        }
        if (modelClass.isAssignableFrom(CoworkerViewModel.class)) {
            return (T) new CoworkerViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}