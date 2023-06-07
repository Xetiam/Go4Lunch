package com.example.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.restaurant.RestaurantDataSource;
import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.RetrofitService;
import com.example.go4lunch.data.user.UserDataSource;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.ui.RestaurantViewModel;
import com.example.go4lunch.ui.coworker.CoworkerViewModel;
import com.example.go4lunch.ui.login.LoginViewModel;
import com.example.go4lunch.ui.restaurantdetail.RestaurantDetailViewModel;
import com.example.go4lunch.ui.restaurantlist.RestaurantsListViewModel;
import com.example.go4lunch.ui.restaurantmap.RestaurantMapViewModel;
import com.example.go4lunch.ui.settings.SettingsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PlacesRepository placesRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    private ViewModelFactory() {
        placesRepository = new PlacesRepository(
                RetrofitService.getPlacesApi()
        );
        userRepository = new UserRepository(new UserDataSource());
        restaurantRepository = new RestaurantRepository(new RestaurantDataSource());
    }

    public static ViewModelFactory getInstance() {
        return FactoryHolder.factory;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantMapViewModel.class)) {
            return (T) new RestaurantMapViewModel(placesRepository,restaurantRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantsListViewModel.class)) {
            return (T) new RestaurantsListViewModel(restaurantRepository);
        }
        if (modelClass.isAssignableFrom(CoworkerViewModel.class)) {
            return (T) new CoworkerViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)) {
            return (T) new RestaurantDetailViewModel(placesRepository, restaurantRepository, userRepository);
        }
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(userRepository, restaurantRepository);
        }
        if (modelClass.isAssignableFrom(SettingsViewModel.class)) {
            return (T) new SettingsViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

    private static final class FactoryHolder {
        static final ViewModelFactory factory = new ViewModelFactory();
    }
}