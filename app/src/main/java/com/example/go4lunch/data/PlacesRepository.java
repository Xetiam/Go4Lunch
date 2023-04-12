package com.example.go4lunch.data;


import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.pojo.PlacesDetailsResponse;
import com.example.go4lunch.data.pojo.PlacesNearbySearchResponse;
import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepository {
    private final PlacesApi placesApi;
    private final String FIELD_DETAIL = "opening_hours,website,formatted_phone_number,place_id";//TODO: manque le type de restaurant
    private PlacesNearbySearchResponse alreadyFetchedResponse = null;

    public PlacesRepository(PlacesApi placesApi) {
        this.placesApi = placesApi;
    }

    public MutableLiveData<List<RestaurantEntity>> getPlacesLiveData(LatLng userLocation) {
        MutableLiveData<List<RestaurantEntity>> placesMutableLiveData = new MutableLiveData<>();
        PlacesNearbySearchResponse response = alreadyFetchedResponse;

        if (response != null) {
            placesMutableLiveData.setValue(response.toDomain());
        } else {
            placesApi.getPlacesNearbySearch((userLocation.latitude
                            + ","
                            + userLocation.longitude),
                    1500,
                    false,
                    "restaurant",
                    MAPS_API_KEY).enqueue(new Callback<PlacesNearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<PlacesNearbySearchResponse> call, @NonNull Response<PlacesNearbySearchResponse> response) {
                    if (response.body() != null) {
                        alreadyFetchedResponse = response.body();
                        placesMutableLiveData.setValue(response.body().toDomain());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PlacesNearbySearchResponse> call, @NonNull Throwable t) {
                    placesMutableLiveData.setValue(null);
                }
            });
        }

        return placesMutableLiveData;
    }

    public MutableLiveData<RestaurantDetailEntity> getPlaceDetailLiveData(String placeId) {
        MutableLiveData<RestaurantDetailEntity> placeDetailMutableLiveData = new MutableLiveData<>();
        placesApi.getPlaceDetail(placeId,
                FIELD_DETAIL,
                MAPS_API_KEY).enqueue(new Callback<PlacesDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlacesDetailsResponse> call, @NonNull Response<PlacesDetailsResponse> response) {
                if (response.body() != null) {
                    placeDetailMutableLiveData.setValue(response.body().toDomain());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesDetailsResponse> call, @NonNull Throwable t) {
                placeDetailMutableLiveData.setValue(null);
            }
        });
        return placeDetailMutableLiveData;
    }
}

