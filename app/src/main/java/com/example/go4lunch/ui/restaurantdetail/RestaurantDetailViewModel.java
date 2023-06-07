package com.example.go4lunch.ui.restaurantdetail;

import static com.example.go4lunch.ui.settings.SettingsActivity.IS_ACTIVATED;
import static com.example.go4lunch.ui.settings.SettingsActivity.NOTIFICATIONS_PREFERENCES;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepositoryContract;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.data.user.UserRepositoryContract;
import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.DetailCallback;

import java.util.ArrayList;

public class RestaurantDetailViewModel extends ViewModel implements DetailCallback {
    private final PlacesRepository placesRepository;
    private final RestaurantRepositoryContract restaurantRepository;
    private final UserRepositoryContract userRepository;
    private final MutableLiveData<RestaurantDetailState> _state = new MutableLiveData<>();
    public LiveData<RestaurantDetailState> state = _state;
    private String restaurantId;
    private boolean isEvaluate;
    private boolean shoulSetNotification;

    public RestaurantDetailViewModel(PlacesRepository placesRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.placesRepository = placesRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public void initDetail(String restaurantId) {
        this.restaurantId = restaurantId;
        LiveData<WithResponseState> responseLiveData = Transformations.map(placesRepository.getPlaceDetailLiveData(restaurantId),
                this::mapDataToViewState
        );
        responseLiveData.observeForever(this::setStateOnResponse);
    }

    public void initEvaluation() {
        userRepository.getCurrentUserEvaluations(this, restaurantId);
    }

    public void initLunchers() {
        restaurantRepository.getLunchers(restaurantId, userRepository.getCurrentUserUID(), this);
    }

    private WithResponseState mapDataToViewState(RestaurantDetailEntity restaurantDetailEntity) {
        return new WithResponseState(restaurantDetailEntity);
    }

    private void setStateOnResponse(WithResponseState withResponseState) {
        _state.postValue(withResponseState);
    }

    public void addOrRemoveEvaluation() {
        restaurantRepository.addOrRemoveAnEvaluationOnRestaurantAndUser(
                restaurantId,
                userRepository.getCurrentUserUID(),
                isEvaluate,
                this);
    }

    public void selectOrCancelLunch(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NOTIFICATIONS_PREFERENCES, Context.MODE_PRIVATE);
        shoulSetNotification = preferences.getBoolean(IS_ACTIVATED,false);
        userRepository.updateLunchChoiceOnUserAndPreviousRestaurant(restaurantId, this);
    }

    @Override
    public void lunchersCallback(ArrayList<String> lunchers) {
        if (lunchers.size() > 0) {
            userRepository.getUsersLuncherByIds(lunchers, this);
        }
    }

    @Override
    public void userCallback(ArrayList<UserEntity> users) {
        _state.postValue(new LuncherState(users));
    }

    @Override
    public void evaluationsCallback(boolean isEvaluate) {
        this.isEvaluate = isEvaluate;
        this._state.postValue(new HasEvaluate(isEvaluate));
    }

    @Override
    public void isLuncherCallback(boolean isLuncher) {
        boolean setNotification;
        setNotification = isLuncher && shoulSetNotification;
        _state.postValue(new CurrentUserLunchState(isLuncher, setNotification));
        shoulSetNotification = false;
    }
}
