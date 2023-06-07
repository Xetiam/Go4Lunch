package com.example.go4lunch.ui.coworker;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.user.UserRepositoryContract;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoworkerViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepositoryContract mockUserRepository;

    private CoworkerViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new CoworkerViewModel(mockUserRepository);
    }

    @Test
    public void initCoworker_success() {
        // Given
        List<UserEntity> userEntities = new ArrayList<>(Arrays.asList(DataUtils.getDefaultCurrentUser()));
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();

        // When
        viewModel.coworkerCallback(new ArrayList<>(userEntities), new ArrayList<>(restaurantEntities));

        // Then
        UserResponseState state = (UserResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(userEntities, state.getUserEntities());
        assertEquals(restaurantEntities, state.getRestaurantEntities());
    }

    @Test
    public void search_withValidInput() {
        // Given
        List<UserEntity> userEntities = new ArrayList<>(Arrays.asList(DataUtils.getDefaultCurrentUser()));
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        viewModel.coworkerCallback(new ArrayList<>(userEntities), new ArrayList<>(restaurantEntities));

        // When
        viewModel.search("restA");

        // Then
        UserResponseState state = (UserResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(1, state.getRestaurantEntities().size());
        assertEquals("restA", state.getRestaurantEntities().get(0).getRestaurantname());
    }

    @Test
    public void search_withEmptyInput() {
        // Init
        List<UserEntity> userEntities = new ArrayList<>(Arrays.asList(DataUtils.getDefaultCurrentUser()));
        List<RestaurantEntity> restaurantEntities = DataUtils.getDefaultRestaurantsByPosition();
        viewModel.coworkerCallback(new ArrayList<>(userEntities), new ArrayList<>(restaurantEntities));
        viewModel.coworkerCallback(new ArrayList<>(userEntities), new ArrayList<>(restaurantEntities));

        // When
        viewModel.search("");

        // Then
        UserResponseState state = (UserResponseState) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertEquals(userEntities, state.getUserEntities());
        assertEquals(restaurantEntities, state.getRestaurantEntities());
    }
}
