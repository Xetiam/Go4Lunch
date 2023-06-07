package com.example.go4lunch.ui.restaurantmap;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class RestaurantMapViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @Mock
    private PlacesRepository mockPlacesRepository;

    private RestaurantMapViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RestaurantMapViewModel(mockPlacesRepository, mockRestaurantRepository);
    }

    @Test
    public void should_get_restaurant_by_user_position() {
        //Given
        LatLng userPos = DataUtils.getDefaultCurrentUserPosition();
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        Mockito.when(mockPlacesRepository.getPlacesLiveData(userPos)).thenReturn(new MutableLiveData<>(restaurantEntities));
        //When
        viewModel.getResponseLiveData(userPos);
        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurants(), restaurantEntities);
    }

    @Test
    public void should_filter_restaurants_by_query() {
        //Given
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        LatLng userPos = DataUtils.getDefaultCurrentUserPosition();
        Mockito.when(mockPlacesRepository.getPlacesLiveData(userPos)).thenReturn(new MutableLiveData<>(restaurantEntities));
        //When
        viewModel.getResponseLiveData(userPos);
        viewModel.search("B");
        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        restaurantEntities.remove(1);
        restaurantEntities.remove(1);

        assertEquals(state.getRestaurants(), restaurantEntities);
    }
}
