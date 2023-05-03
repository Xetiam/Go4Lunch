package com.example.go4lunch.data;

import com.example.go4lunch.data.pojo.PlacesDetailsResponse;
import com.example.go4lunch.data.pojo.PlacesNearbySearchResponse;

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

    @GET("details/json?")
    Call<PlacesDetailsResponse> getPlaceDetail(@Query("place_id") String placeId,
                                               @Query("fields") String fields,
                                               @Query("key") String apiKey);
}