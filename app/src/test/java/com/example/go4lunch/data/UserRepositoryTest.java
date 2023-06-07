package com.example.go4lunch.data;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.example.go4lunch.data.user.UserDataSource;
import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.SignOutCallback;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryTest {

    @Mock
    private UserDataSource mockDataSource;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository(mockDataSource);
    }

    @Test
    public void getCurrentUser_returnsCurrentUserFromDataSource() {
        // Given
        UserEntity currentUser = new UserEntity("user1", "John Doe", null, "", new ArrayList<>());
        Mockito.when(mockDataSource.getCurrentUserData()).thenReturn(currentUser);

        // When
        UserEntity result = userRepository.getCurrentUser();

        // Then
        assert result == currentUser;
        verify(mockDataSource).getCurrentUserData();
    }

    @Test
    public void getCurrentUserUID_returnsCurrentUserUIDFromDataSource() {
        // Given
        String uid = "user1";
        UserEntity currentUser = new UserEntity(uid, "John Doe", null, "", new ArrayList<>());
        Mockito.when(mockDataSource.getCurrentUserData()).thenReturn(currentUser);

        // When
        String result = userRepository.getCurrentUserUID();

        // Then
        assert result.equals(uid);
        verify(mockDataSource).getCurrentUserData();
    }

    @Test
    public void getCurrentUserMail_returnsCurrentUserMailFromDataSource() {
        // Given
        String mail = "john@example.com";
        Mockito.when(mockDataSource.getCurrentUserMailData()).thenReturn(mail);

        // When
        String result = userRepository.getCurrentUserMail();

        // Then
        assert result.equals(mail);
        verify(mockDataSource).getCurrentUserMailData();
    }

    @Test
    public void createUser_callsCreateUserDataOnDataSource() {
        // Given
        UserEntity currentUser = new UserEntity("user1", "John Doe", null, "", new ArrayList<>());
        Mockito.when(mockDataSource.getCurrentUserData()).thenReturn(currentUser);

        // When
        userRepository.createUser();

        // Then
        verify(mockDataSource).createUserData(currentUser);
    }

    @Test
    public void updateUsername_callsUpdateUsernameDataOnDataSource() {
        // Given
        String username = "John Doe";
        String uid = "user1";

        UserEntity mockUserEntity = Mockito.mock(UserEntity.class);
        Mockito.when(mockUserEntity.getUid()).thenReturn(uid);

        Mockito.when(mockDataSource.getCurrentUserData()).thenReturn(mockUserEntity);

        // When
        userRepository.updateUsername(username);

        // Then
        verify(mockDataSource).updateUsernameData(username, uid);
    }

    @Test
    public void updateLunchChoiceOnUserAndPreviousRestaurant_callsUpdateLunchChoiceDataOnDataSource() {
        // Given
        String restaurantId = "restaurant1";
        DetailCallback callback = Mockito.mock(DetailCallback.class);

        // When
        userRepository.updateLunchChoiceOnUserAndPreviousRestaurant(restaurantId, callback);

        // Then
        verify(mockDataSource).updateLunchChoiceData(restaurantId, callback);
    }

    @Test
    public void deleteUserFromFirestore_callsDeleteUserDataOnDataSource() {
        // Given
        SignOutCallback callback = Mockito.mock(SignOutCallback.class);
        String uid = "user1";

        UserEntity mockUserEntity = Mockito.mock(UserEntity.class);
        Mockito.when(mockUserEntity.getUid()).thenReturn(uid);

        Mockito.when(mockDataSource.getCurrentUserData()).thenReturn(mockUserEntity);

        // When
        userRepository.deleteUserFromFirestore(callback);

        // Then
        verify(mockDataSource).deleteUserData(callback);
    }

    @Test
    public void getCurrentUserEvaluations_callsGetCurrentUserEvaluationsDataOnDataSource() {
        // Given
        String restaurantId = "restaurant1";
        DetailCallback callback = Mockito.mock(DetailCallback.class);

        // When
        userRepository.getCurrentUserEvaluations(callback, restaurantId);

        // Then
        verify(mockDataSource).getCurrentUserEvaluationsData(restaurantId, callback);
    }

    @Test
    public void getUsersLuncherByIds_callsGetUsersLuncherByIdsDataOnDataSource() {
        // Given
        ArrayList<String> lunchers = new ArrayList<>();
        lunchers.add("user1");
        lunchers.add("user2");
        DetailCallback callback = Mockito.mock(DetailCallback.class);

        // When
        userRepository.getUsersLuncherByIds(lunchers, callback);

        // Then
        verify(mockDataSource).getUSersLuncherByIdsData(lunchers, callback, userRepository);
    }

    @Test
    public void getAllUserAndRestaurants_callsGetUsersAndRestaurantsDataOnDataSource() {
        // Given
        CoworkerCallback callback = Mockito.mock(CoworkerCallback.class);

        // When
        userRepository.getAllUserAndRestaurants(callback);

        // Then
        verify(mockDataSource).getUsersAndRestaurantsData(callback, userRepository);
    }

    @Test
    public void getCurrentUserLunch_callsGetCurrentUserLunchDataOnDataSource() {
        // Given
        RestaurantCallback callback = Mockito.mock(RestaurantCallback.class);

        // When
        userRepository.getCurrentUserLunch(callback);

        // Then
        verify(mockDataSource).getCurrentUserLunchData(callback);
    }

    @Test
    public void usersData_callsUserCallbackOnDetailCallback() {
        // Given
        DetailCallback callback = Mockito.mock(DetailCallback.class);
        ArrayList<Map<String, Object>> userDocuments = new ArrayList<>();
        Map<String, Object> userDocument = new HashMap<>();
        userDocuments.add(userDocument);

        // When
        userRepository.usersData(userDocuments, callback);

        // Then
        verify(callback).userCallback(any(ArrayList.class));
    }

    @Test
    public void usersAndRestaurantsData_callsCoworkerCallbackOnDetailCallback() {
        // Given
        CoworkerCallback callback = Mockito.mock(CoworkerCallback.class);
        ArrayList<Map<String, Object>> userDocuments = new ArrayList<>();
        Map<String, Object> userDocument = new HashMap<>();
        userDocuments.add(userDocument);
        ArrayList<Map<String, Object>> restaurantDocuments = new ArrayList<>();
        Map<String, Object> restaurantDocument = new HashMap<>();

        GeoPoint mockGeoPoint = Mockito.mock(GeoPoint.class);

        restaurantDocument.put("restaurantposition", mockGeoPoint);

        restaurantDocuments.add(restaurantDocument);

        // When
        userRepository.usersAndRestaurantsData(userDocuments, restaurantDocuments, callback);

        // Then
        verify(callback).coworkerCallback(any(ArrayList.class), any(ArrayList.class));
    }
}
