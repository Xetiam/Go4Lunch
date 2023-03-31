package com.example.go4lunch.ui.restaurantdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.RestaurantRepository;
import com.example.go4lunch.data.RestaurantRepositoryContract;
import com.example.go4lunch.data.UserRepository;
import com.example.go4lunch.data.UserRepositoryContract;
import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.ui.restaurantdetail.WithResponseState;

import java.util.ArrayList;

public class RestaurantDetailViewModel extends ViewModel {
    private final PlacesRepository placesRepository;
    private final RestaurantRepositoryContract restaurantRepository;
    private final UserRepositoryContract userRepository;
    public MutableLiveData<RestaurantDetailState> _state = new MutableLiveData<>();
    public LiveData<RestaurantDetailState> state = _state;

    public RestaurantDetailViewModel(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
        this.restaurantRepository = new RestaurantRepository();
        this.userRepository = new UserRepository();
    }

    public void initDetail(String restaurantId) {
        userRepository.getCurrentUserEvaluations((ArrayList<String> evaluations) -> {
            if (evaluations.contains(restaurantId)) {
                this._state.postValue(new HasEvaluate(true));
            } else {
                this._state.postValue(new HasEvaluate(false));
            }
        });
        LiveData<WithResponseState> responseLiveData = Transformations.map(placesRepository.getPlaceDetailLiveData(restaurantId),
                this::mapDataToViewState
        );
        responseLiveData.observeForever(this::setStateOnResponse);
    }

    private WithResponseState mapDataToViewState(RestaurantDetailEntity restaurantDetailEntity) {
        return new WithResponseState(restaurantDetailEntity);
    }

    private void setStateOnResponse(WithResponseState withResponseState) {
        _state.setValue(withResponseState);
    }

    public void addOrRemoveEvaluation(String restaurantId) {
        userRepository.getCurrentUserEvaluations((ArrayList<String> evaluations) -> {
            restaurantRepository.addOrRemoveAnEvaluation(restaurantId,evaluations.contains(restaurantId));
            userRepository.addOrRemoveRestaurantEvaluation(restaurantId, evaluations.contains(restaurantId));
            this._state.postValue(new HasEvaluate(!evaluations.contains(restaurantId)));
        });
    }
}
