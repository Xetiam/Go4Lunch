package com.example.go4lunch.ui;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.example.go4lunch.utils.RestaurantCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class RestaurantViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RestaurantRepository mockRestaurantRepository;

    private RestaurantViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new RestaurantViewModel(mockUserRepository, mockRestaurantRepository);
    }

    @Test
    public void should_load_user_to_drawer() {
        //Given
        Mockito.when(mockUserRepository.getCurrentUser()).thenReturn(DataUtils.getDefaultCurrentUser());
        Mockito.when(mockUserRepository.getCurrentUserMail()).thenReturn(DataUtils.getDefaultCurrentUserMail());

        //When
        viewModel.setUserDrawer();

        //Then
        UserDrawerState state = (UserDrawerState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getUser(), DataUtils.getDefaultCurrentUser());
        assertEquals(state.getUserMail(), DataUtils.getDefaultCurrentUserMail());
    }

    @Test
    public void should_get_user_lunch_choice() {
        // Given
        String lunchChoice = DataUtils.getDefaultCurrentUserLuncher();
        RestaurantEntity restaurant = DataUtils.getDefaultCurrentUserRestaurant();

        Mockito.doAnswer(invocation -> {
            RestaurantCallback callback = invocation.getArgument(0);
            callback.userCallback(lunchChoice);
            return null;
        }).when(mockUserRepository).getCurrentUserLunch(Mockito.any(RestaurantCallback.class));

        Mockito.doAnswer(invocation -> {
            RestaurantCallback callback = invocation.getArgument(1);
            callback.restaurantCallback(restaurant);
            return null;
        }).when(mockRestaurantRepository).getRestaurantById(
                Mockito.eq(lunchChoice),
                Mockito.any(RestaurantCallback.class)
        );

        // When
        viewModel.onMyLunchClick();

        // Then
        Mockito.verify(mockRestaurantRepository).getRestaurantById(eq(lunchChoice), Mockito.any(RestaurantCallback.class));
        LunchChoiceState state = (LunchChoiceState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(state.getLunchChoice(), restaurant);
    }

    @Test
    public void should_not_get_user_lunch_choice() {
        // Given
        Mockito.doAnswer(invocation -> {
            RestaurantCallback callback = invocation.getArgument(0);
            callback.userCallback(null);
            return null;
        }).when(mockUserRepository).getCurrentUserLunch(Mockito.any(RestaurantCallback.class));

        // When
        viewModel.onMyLunchClick();

        // Then
        Mockito.verify(mockRestaurantRepository, Mockito.never()).getRestaurantById(Mockito.any(String.class), Mockito.any(RestaurantCallback.class));
        RestaurantState state = LiveDataTestUtils.getValueForTesting(viewModel.state);
        assert (state instanceof NoLunchChoiceState);
    }
}
