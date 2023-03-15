package com.example.go4lunch.data;

import com.example.go4lunch.data.pojo.PlacesNearbySearchResponse;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApi {
    @GET("nearbysearch/json?")
    Call<PlacesNearbySearchResponse> getPlacesNearbySearch(@Query("location") String userLocation,
                                                           @Query("radius") int radius,
                                                           @Query("sensor") Boolean sensor,
                                                           @Query("type") String typeSearch,
                                                           @Query("key") String apiKey);
}