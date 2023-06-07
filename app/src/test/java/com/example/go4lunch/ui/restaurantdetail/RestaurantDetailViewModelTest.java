package com.example.go4lunch.ui.restaurantdetail;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.data.PlacesRepository;
import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.model.RestaurantDetailEntity;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

public class RestaurantDetailViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    @Mock
    private PlacesRepository mockPlacesRepository;
    private RestaurantDetailViewModel viewModel;
    private RestaurantEntity restaurant;
    private RestaurantDetailEntity restaurantDetail;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RestaurantDetailViewModel(mockPlacesRepository, mockRestaurantRepository, mockUserRepository);


        restaurant = DataUtils.getDefaultCurrentUserRestaurant();
        ArrayList<String> opening_hours = new ArrayList<>(Arrays.asList("lundi: 8h-18h", "mardi: 10h-20h"));
        restaurantDetail = new RestaurantDetailEntity(
                "www.urltype.com",
                "06.61.01.02.03",
                opening_hours,
                restaurant.getRestaurantId()
        );
        Mockito.when(mockPlacesRepository.getPlaceDetailLiveData(restaurant.getRestaurantId())).thenReturn(
                new MutableLiveData<>(restaurantDetail)
        );
        viewModel.initDetail(restaurant.getRestaurantId());
    }

    @Test
    public void should_init_restaurant_detail_view() {
        //verify
        WithResponseState state = (WithResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getRestaurantDetailEntity(), restaurantDetail);
    }

    @Test
    public void should_init_evaluation_and_current_user_has_evaluate() {
        //Given
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(0);
            callback.evaluationsCallback(true);
            return null;
        }).when(mockUserRepository).getCurrentUserEvaluations(
                Mockito.any(DetailCallback.class),
                eq(restaurant.getRestaurantId())
        );

        //When
        viewModel.initEvaluation();

        //Then
        Mockito.verify(mockUserRepository).getCurrentUserEvaluations(
                Mockito.any(DetailCallback.class),
                eq(restaurant.getRestaurantId())
        );

        HasEvaluate state = (HasEvaluate) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(true, state.isEvaluate());
    }

    @Test
    public void should_init_evaluation_and_current_user_has_not_evaluate() {
        //Given
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(0);
            callback.evaluationsCallback(false);
            return null;
        }).when(mockUserRepository).getCurrentUserEvaluations(
                Mockito.any(DetailCallback.class),
                eq(restaurant.getRestaurantId())
        );

        //When
        viewModel.initEvaluation();

        //Then
        Mockito.verify(mockUserRepository).getCurrentUserEvaluations(
                Mockito.any(DetailCallback.class),
                eq(restaurant.getRestaurantId())
        );

        HasEvaluate state = (HasEvaluate) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(false, state.isEvaluate());
    }

    @Test
    public void should_init_lunchchoice_and_current_user_has_chosen() {
        //Given
        ArrayList<String> lunchers = new ArrayList<>(Arrays.asList("uid", "user2", "user3"));
        ArrayList<UserEntity> lunchersEntity = new ArrayList<>(
                Arrays.asList(DataUtils.getDefaultCurrentUser(),
                        DataUtils.getDefaultCurrentUser(),
                        DataUtils.getDefaultCurrentUser())
        );
        Mockito.when(mockUserRepository.getCurrentUserUID()).thenReturn(DataUtils.getDefaultCurrentUser().getUid());
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(2);
            callback.lunchersCallback(lunchers);
            callback.isLuncherCallback(true);
            return null;
        }).when(mockRestaurantRepository).getLunchers(
                eq(restaurant.getRestaurantId()),
                eq(DataUtils.getDefaultCurrentUser().getUid()),
                Mockito.any(DetailCallback.class)
        );
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(1);
            callback.userCallback(lunchersEntity);
            return null;
        }).when(mockUserRepository).getUsersLuncherByIds(
                eq(lunchers),
                Mockito.any(DetailCallback.class)
        );

        //When
        viewModel.initLunchers();

        //Then
        Mockito.verify(mockRestaurantRepository).getLunchers(eq(restaurant.getRestaurantId()),
                eq(DataUtils.getDefaultCurrentUser().getUid()),
                Mockito.any(DetailCallback.class));
        CurrentUserLunchState state = (CurrentUserLunchState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(true, state.isCurrentUserLuncher());
    }

    @Test
    public void should_init_lunchchoice_and_current_user_has_not_chosen() {
        //Given
        ArrayList<String> lunchers = new ArrayList<>(Arrays.asList("user2", "user3"));
        ArrayList<UserEntity> lunchersEntity = new ArrayList<>(
                Arrays.asList(DataUtils.getDefaultCurrentUser(),
                        DataUtils.getDefaultCurrentUser(),
                        DataUtils.getDefaultCurrentUser())
        );
        Mockito.when(mockUserRepository.getCurrentUserUID()).thenReturn(DataUtils.getDefaultCurrentUser().getUid());
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(2);
            callback.lunchersCallback(lunchers);
            callback.isLuncherCallback(false);
            return null;
        }).when(mockRestaurantRepository).getLunchers(
                eq(restaurant.getRestaurantId()),
                eq(DataUtils.getDefaultCurrentUser().getUid()),
                Mockito.any(DetailCallback.class)
        );
        Mockito.doAnswer(invocation -> {
            DetailCallback callback = invocation.getArgument(1);
            callback.userCallback(lunchersEntity);
            return null;
        }).when(mockUserRepository).getUsersLuncherByIds(
                eq(lunchers),
                Mockito.any(DetailCallback.class)
        );

        //When
        viewModel.initLunchers();

        //Then
        Mockito.verify(mockRestaurantRepository).getLunchers(eq(restaurant.getRestaurantId()),
                eq(DataUtils.getDefaultCurrentUser().getUid()),
                Mockito.any(DetailCallback.class));
        CurrentUserLunchState state = (CurrentUserLunchState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(false, state.isCurrentUserLuncher());
    }
}
