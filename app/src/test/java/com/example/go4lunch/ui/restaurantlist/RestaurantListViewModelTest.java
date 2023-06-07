package com.example.go4lunch.ui.restaurantlist;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.example.go4lunch.utils.RestaurantsCallback;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantListViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    private RestaurantsListViewModel viewModel;
    LatLng userPos;


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RestaurantsListViewModel(mockRestaurantRepository);
        userPos = DataUtils.getDefaultCurrentUserPosition();
    }


    @Test
    public void should_sort_restaurant_list_by_eval() {
        //Given
        LatLng userPos = DataUtils.getDefaultCurrentUserPosition();
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        Mockito.doAnswer(invocation -> {
            RestaurantsCallback callback = invocation.getArgument(0);
            callback.restaurantsCallback(restaurantEntities);
            return null;
        }).when(mockRestaurantRepository).getRestaurants(
                Mockito.any(RestaurantsCallback.class)
        );

        //When
        viewModel.initRestaurantList(userPos);
        viewModel.sortRestaurants(1);

        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurants(), DataUtils.getDefaultRestaurantsByEval());

    }

    @Test
    public void should_init_restaurant_list() {
        //Given
        LatLng userPos = DataUtils.getDefaultCurrentUserPosition();
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        Mockito.doAnswer(invocation -> {
            RestaurantsCallback callback = invocation.getArgument(0);
            callback.restaurantsCallback(restaurantEntities);
            return null;
        }).when(mockRestaurantRepository).getRestaurants(
                Mockito.any(RestaurantsCallback.class)
        );

        //When
        viewModel.initRestaurantList(userPos);

        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurants(), restaurantEntities);
    }


    @Test
    public void should_sort_restaurant_list_by_position() {
        //Given
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByEval();
        Mockito.doAnswer(invocation -> {
            RestaurantsCallback callback = invocation.getArgument(0);
            callback.restaurantsCallback(restaurantEntities);
            return null;
        }).when(mockRestaurantRepository).getRestaurants(
                Mockito.any(RestaurantsCallback.class)
        );

        //When
        viewModel.initRestaurantList(userPos);
        viewModel.sortRestaurants(1);
        viewModel.sortRestaurants(0);


        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurants(), DataUtils.getDefaultRestaurantsByPosition());
    }

    @Test
    public void should_sort_restaurant_list_by_luncher() {
        //Given
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        Mockito.doAnswer(invocation -> {
            RestaurantsCallback callback = invocation.getArgument(0);
            callback.restaurantsCallback(restaurantEntities);
            return null;
        }).when(mockRestaurantRepository).getRestaurants(
                Mockito.any(RestaurantsCallback.class)
        );

        //When
        viewModel.initRestaurantList(userPos);
        viewModel.sortRestaurants(2);

        //Then
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurants().size(), DataUtils.getDefaultRestaurantsByLuncher().size());
    }

    @Test
    public void should_filter_restaurant_list_by_query() {
        //Given
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        Mockito.doAnswer(invocation -> {
            RestaurantsCallback callback = invocation.getArgument(0);
            callback.restaurantsCallback(restaurantEntities);
            return null;
        }).when(mockRestaurantRepository).getRestaurants(
                Mockito.any(RestaurantsCallback.class)
        );

        //When
        viewModel.initRestaurantList(userPos);
        viewModel.search("A");

        //Then
        ArrayList<RestaurantEntity> expected = new ArrayList<>(Arrays.asList(DataUtils.getDefaultRestaurantsByLuncher().get(2)));
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(expected, state.getRestaurants());
    }
}
