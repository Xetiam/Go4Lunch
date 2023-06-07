package com.example.go4lunch.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.data.pojo.PlacesDetailsResponse;
import com.example.go4lunch.data.pojo.PlacesNearbySearchResponse;
import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.RestaurantEntity;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private PlacesApi placesApi;
    @Mock
    private Call<PlacesNearbySearchResponse> nearbySearchCall;
    @Mock
    private Call<PlacesDetailsResponse> detailsCall;

    private PlacesRepository placesRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        placesRepository = new PlacesRepository(placesApi);
    }

    @Test
    public void getPlacesLiveData_withExistingResponse_callsPlacesApiAndReturnsLiveData() {
        // Given
        LatLng userLocation = new LatLng(0, 0);

        MutableLiveData<List<RestaurantEntity>> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(new ArrayList<>());

        when(placesApi.getPlacesNearbySearch(any(), anyInt(), anyBoolean(), anyString(), anyString())).thenReturn(nearbySearchCall);
        doNothing().when(nearbySearchCall).enqueue(any());

        ArgumentCaptor<Callback<PlacesNearbySearchResponse>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        // Create a mock response
        PlacesNearbySearchResponse nearbySearchResponse = Mockito.mock(PlacesNearbySearchResponse.class);
        when(nearbySearchResponse.toDomain()).thenReturn(new ArrayList<>());

        // When
        MutableLiveData<List<RestaurantEntity>> actualLiveData = placesRepository.getPlacesLiveData(userLocation);

        // Then
        assertNotNull(actualLiveData);
        assertNull(actualLiveData.getValue());

        verify(placesApi).getPlacesNearbySearch(
                eq("0.0,0.0"),
                eq(1500),
                eq(false),
                eq("restaurant"),
                eq(BuildConfig.MAPS_API_KEY));

        verify(nearbySearchCall).enqueue(callbackCaptor.capture());
        Callback<PlacesNearbySearchResponse> capturedCallback = callbackCaptor.getValue();
        capturedCallback.onResponse(nearbySearchCall, Response.success(nearbySearchResponse));

        assertEquals(expectedLiveData.getValue(), actualLiveData.getValue());
    }


    @Test
    public void getPlacesLiveData_withNullResponse_callsPlacesApiAndReturnsNullLiveData() {
        // Given
        LatLng userLocation = new LatLng(0, 0);

        when(placesApi.getPlacesNearbySearch(any(), anyInt(), anyBoolean(), anyString(), anyString())).thenReturn(nearbySearchCall);
        doAnswer(invocation -> {
            Callback<PlacesNearbySearchResponse> callback = invocation.getArgument(0);
            callback.onResponse(nearbySearchCall, Response.success(null));
            return null;
        }).when(nearbySearchCall).enqueue(any());

        // When
        MutableLiveData<List<RestaurantEntity>> actualLiveData = placesRepository.getPlacesLiveData(userLocation);

        // Then
        assertNotNull(actualLiveData);
        assertNull(actualLiveData.getValue());

        verify(placesApi).getPlacesNearbySearch(
                eq("0.0,0.0"),
                eq(1500),
                eq(false),
                eq("restaurant"),
                eq(BuildConfig.MAPS_API_KEY));

        assertNull(actualLiveData.getValue());
    }

    @Test
    public void getPlaceDetailLiveData_withExistingResponse_callsPlacesApiAndReturnsLiveData() {
        // Given
        String placeId = "place1";
        String fields = "opening_hours,website,formatted_phone_number,place_id";
        String apiKey = BuildConfig.MAPS_API_KEY;

        MutableLiveData<RestaurantDetailEntity> expectedLiveData = new MutableLiveData<>();
        RestaurantDetailEntity expectedDetailEntity = new RestaurantDetailEntity("name", "address", null, "phone");
        expectedLiveData.setValue(expectedDetailEntity);

        when(placesApi.getPlaceDetail(placeId, fields, apiKey)).thenReturn(detailsCall);

        ArgumentCaptor<Callback<PlacesDetailsResponse>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        // Create a mock response
        PlacesDetailsResponse detailsResponse = Mockito.mock(PlacesDetailsResponse.class);
        when(detailsResponse.toDomain()).thenReturn(expectedDetailEntity);

        // When
        MutableLiveData<RestaurantDetailEntity> actualLiveData = placesRepository.getPlaceDetailLiveData(placeId);

        // Then
        assertNotNull(actualLiveData);
        assertNull(actualLiveData.getValue());

        verify(placesApi).getPlaceDetail(placeId, fields, apiKey);
        verify(detailsCall).enqueue(callbackCaptor.capture());
        Callback<PlacesDetailsResponse> capturedCallback = callbackCaptor.getValue();
        capturedCallback.onResponse(detailsCall, Response.success(detailsResponse));

        assertEquals(expectedDetailEntity, actualLiveData.getValue());
    }

    @Test
    public void getPlaceDetailLiveData_withNullResponse_callsPlacesApiAndReturnsNullLiveData() {
        // Given
        String placeId = "place1";

        MutableLiveData<RestaurantDetailEntity> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(null);

        when(placesApi.getPlaceDetail(eq(placeId), anyString(), anyString())).thenReturn(detailsCall);
        doNothing().when(detailsCall).enqueue(any());

        ArgumentCaptor<Callback<PlacesDetailsResponse>> callbackCaptor = ArgumentCaptor.forClass(Callback.class);

        // When
        MutableLiveData<RestaurantDetailEntity> actualLiveData = placesRepository.getPlaceDetailLiveData(placeId);

        // Then
        assertNotNull(actualLiveData);
        assertEquals(expectedLiveData.getValue(), actualLiveData.getValue());

        verify(placesApi).getPlaceDetail(
                eq(placeId),
                eq("opening_hours,website,formatted_phone_number,place_id"),
                eq(BuildConfig.MAPS_API_KEY));

        verify(detailsCall).enqueue(callbackCaptor.capture());
        Callback<PlacesDetailsResponse> capturedCallback = callbackCaptor.getValue();
        capturedCallback.onResponse(detailsCall, Response.success(null));

        assertNull(actualLiveData.getValue());
    }
}
