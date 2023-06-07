package com.example.go4lunch.data;

import static com.example.go4lunch.data.restaurant.RestaurantDataSource.RESTAURANT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.restaurant.RestaurantDataSource;
import com.example.go4lunch.data.restaurant.RestaurantRepository;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.RestaurantsCallback;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantsCallback mockRestaurantsCallback;
    @Mock
    private DetailCallback mockDetailCallback;
    @Mock
    private RestaurantCallback mockRestaurantCallback;
    @Mock
    private RestaurantDataSource mockDataSource;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        restaurantRepository = new RestaurantRepository(mockDataSource);
    }

    @Test
    public void getRestaurants_callsFireBaseDataSource() {
        // When
        restaurantRepository.getRestaurants(mockRestaurantsCallback);

        // Then
        verify(mockDataSource).getRestaurantsData(restaurantRepository, mockRestaurantsCallback);
    }

    @Test
    public void getLunchers_callsGetLunchersData() {
        // Given
        String restaurantId = "restaurant1";
        String userId = "user1";

        // When
        restaurantRepository.getLunchers(restaurantId, userId, mockDetailCallback);

        // Then
        verify(mockDataSource).getLunchersData(restaurantId, userId, restaurantRepository, mockDetailCallback);
    }

    @Test
    public void getRestaurantById_callsGetRestaurantByIdData() {
        // Given
        String lunchChoice = "restaurant1";

        // When
        restaurantRepository.getRestaurantById(lunchChoice, mockRestaurantCallback);

        // Then
        verify(mockDataSource).getRestaurantByIdData(lunchChoice, restaurantRepository, mockRestaurantCallback);
    }

    @Test
    public void createRestaurants_callsPutRestaurantData() {
        // Given
        ArrayList<RestaurantEntity> restaurantEntities = new ArrayList<>();
        restaurantEntities.add(new RestaurantEntity("1", "Restaurant 1", "", "", new GeoPoint(0, 0), 0L, "", new ArrayList<>()));
        restaurantEntities.add(new RestaurantEntity("2", "Restaurant 2", "", "", new GeoPoint(0, 0), 0L, "", new ArrayList<>()));
        restaurantEntities.add(new RestaurantEntity("3", "Restaurant 3", "", "", new GeoPoint(0, 0), 0L, "", new ArrayList<>()));

        // When
        restaurantRepository.createRestaurants(restaurantEntities);

        // Then
        verify(mockDataSource, times(restaurantEntities.size())).putRestaurantData(any(Map.class), any(String.class));
    }

    @Test
    public void addOrRemoveAnEvaluationOnRestaurantAndUser_callsAddOrRemoveAnEvaluationOnRestaurantAndUserData() {
        // Given
        String restaurantId = "restaurant1";
        String userId = "user1";
        Boolean remove = false;

        // When
        restaurantRepository.addOrRemoveAnEvaluationOnRestaurantAndUser(restaurantId, userId, remove, mockDetailCallback);

        // Then
        verify(mockDataSource).addOrRemoveAnEvaluationOnRestaurantAndUserData(restaurantId, userId, remove, restaurantRepository, mockDetailCallback);
    }

    @Test
    public void restaurantsData_callsRestaurantsCallback() {
        // Given
        List<Map<String, Object>> mockRestaurantDocuments = createMockRestaurantDocuments();

        // When
        restaurantRepository.restaurantsData((ArrayList<Map<String, Object>>) mockRestaurantDocuments, mockRestaurantsCallback);

        // Then
        verify(mockRestaurantsCallback).restaurantsCallback(any(ArrayList.class));
    }

    @Test
    public void lunchersData_callsLunchersCallback() {
        // Given
        Map<String, Object> mockRestaurantDocument = createMockRestaurantDocument();
        String userId = "user1";

        // When
        restaurantRepository.lunchersData(mockRestaurantDocument, mockDetailCallback, userId);

        // Then
        verify(mockDetailCallback).lunchersCallback(any(ArrayList.class));
    }

    @Test
    public void lunchersData_callsIsLuncherCallback_whenUserIsLuncher() {
        // Given
        Map<String, Object> mockRestaurantDocument = Mockito.mock(Map.class);
        String userId = "user1";
        ArrayList<String> lunchers = new ArrayList<>();
        lunchers.add(userId);
        Mockito.when(mockRestaurantDocument.get(Mockito.eq(RestaurantRepository.RESTAURANT_LUNCHERS_FIELD))).thenReturn(lunchers);
        GeoPoint mockGeoPoint = Mockito.mock(GeoPoint.class);
        Mockito.when(mockGeoPoint.getLatitude()).thenReturn(0D);
        Mockito.when(mockRestaurantDocument.get(Mockito.eq("restaurantposition"))).thenReturn(mockGeoPoint);

        // When
        restaurantRepository.lunchersData(mockRestaurantDocument, mockDetailCallback, userId);

        // Then
        Mockito.verify(mockDetailCallback).isLuncherCallback(true);
    }

    @Test
    public void lunchersData_callsIsLuncherCallback_whenUserIsNotLuncher() {
        // Given
        Map<String, Object> mockRestaurantDocument = createMockRestaurantDocument();
        String userId = "user1";

        // When
        restaurantRepository.lunchersData(mockRestaurantDocument, mockDetailCallback, userId);

        // Then
        verify(mockDetailCallback).isLuncherCallback(false);
    }

    @Test
    public void restaurantData_callsRestaurantCallback() {
        // Given
        Map<String, Object> mockRestaurantDocument = createMockRestaurantDocument();

        // When
        restaurantRepository.restaurantData(mockRestaurantDocument, mockRestaurantCallback);

        // Then
        verify(mockRestaurantCallback).restaurantCallback(any(RestaurantEntity.class));
    }

    @Test
    public void addOrRemoveEvaluationData_callsUpdateDocuments() {
        // Given
        Map<String, Object> mockRestaurantDocument = createMockRestaurantDocument();
        Map<String, Object> mockUserDocument = new HashMap<>();
        Boolean remove = false;

        // When
        restaurantRepository.addOrRemoveEvaluationData(mockRestaurantDocument, mockUserDocument, mockDetailCallback, remove);

        // Then
        verify(mockDataSource).updateDocuments(mockRestaurantDocument, mockUserDocument, mockDetailCallback, true);
    }

    private List<Map<String, Object>> createMockRestaurantDocuments() {
        List<Map<String, Object>> restaurantDocuments = new ArrayList<>();
        Map<String, Object> restaurant1 = createMockRestaurantDocument();
        Map<String, Object> restaurant2 = createMockRestaurantDocument();
        restaurantDocuments.add(restaurant1);
        restaurantDocuments.add(restaurant2);
        return restaurantDocuments;
    }

    private Map<String, Object> createMockRestaurantDocument() {
        Map<String, Object> restaurantDocument = new HashMap<>();
        restaurantDocument.put(RESTAURANT_ID, "restaurant1");
        restaurantDocument.put(RestaurantRepository.RESTAURANT_EVALUATIONS_FIELD, 0L);
        restaurantDocument.put(RestaurantRepository.RESTAURANT_DESCRIPTION_FIELD, "Restaurant 1 Description");
        restaurantDocument.put(RestaurantRepository.RESTAURANT_NAME_FIELD, "Restaurant 1");
        restaurantDocument.put(RestaurantRepository.RESTAURANT_POSITION_FIELD, new GeoPoint(0, 0));
        restaurantDocument.put(RestaurantRepository.RESTAURANT_OPENING_HOURS_FIELD, "10:00 AM - 8:00 PM");
        restaurantDocument.put(RestaurantRepository.RESTAURANT_PICTURE_URL, "https://example.com/restaurant1.jpg");
        restaurantDocument.put(RestaurantRepository.RESTAURANT_LUNCHERS_FIELD, new ArrayList<>());
        return restaurantDocument;
    }
}
