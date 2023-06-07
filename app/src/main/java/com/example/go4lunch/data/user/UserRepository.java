package com.example.go4lunch.data.user;

import static com.example.go4lunch.data.restaurant.RestaurantRepository.getRestaurantFromDocument;

import android.net.Uri;

import com.example.go4lunch.data.callback.CallbackUserDataSource;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.example.go4lunch.utils.CoworkerCallback;
import com.example.go4lunch.utils.DetailCallback;
import com.example.go4lunch.utils.RestaurantCallback;
import com.example.go4lunch.utils.SignOutCallback;

import java.util.ArrayList;
import java.util.Map;

public class UserRepository implements UserRepositoryContract, CallbackUserDataSource {
    public static final String USER_NAME_FIELD = "username";
    public static final String USER_ID_FIELD = "uid";
    public static final String USER_PICTURE_FIELD = "urlpicture";
    public static final String USER_LUNCH_CHOICE_FIELD = "lunchchoice";
    public static final String USER_EVALUATIONS_FIELD = "evaluations";

    private final UserDataSource dataSource;

    public UserRepository(UserDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public UserEntity getCurrentUser() {
        return dataSource.getCurrentUserData();
    }

    public String getCurrentUserUID() {
        UserEntity user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public String getCurrentUserMail() {
        return dataSource.getCurrentUserMailData();
    }

    public void createUser() {
        UserEntity user = getCurrentUser();
        if (user != null) {
            dataSource.createUserData(user);
        }
    }

    @Override
    public void updateUsername(String username) {
        dataSource.updateUsernameData(username, this.getCurrentUserUID());
    }

    @Override
    public void updateLunchChoiceOnUserAndPreviousRestaurant(String restaurantId, DetailCallback callback) {
        dataSource.updateLunchChoiceData(restaurantId, callback);
    }

    @Override
    public void deleteUserFromFirestore(SignOutCallback callback) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            dataSource.deleteUserData(callback);
        }
    }

    @Override
    public void getCurrentUserEvaluations(DetailCallback callback, String restaurantId) {
        dataSource.getCurrentUserEvaluationsData(restaurantId, callback);
    }

    @Override
    public void getUsersLuncherByIds(ArrayList<String> lunchers, DetailCallback callback) {
        dataSource.getUSersLuncherByIdsData(lunchers, callback, this);
    }

    @Override
    public void getAllUserAndRestaurants(CoworkerCallback callback) {
        dataSource.getUsersAndRestaurantsData(callback, this);
    }

    public void getCurrentUserLunch(RestaurantCallback callback) {
        dataSource.getCurrentUserLunchData(callback);

    }

    private UserEntity getUserEntityFromDocument(Map<String, Object> document) {
        String id = "";
        String name = "";
        Uri picture = Uri.parse("");
        String lunchChoice = "";
        ArrayList<String> evals = new ArrayList<>();
        if (document != null) {
            id = (String) document.get(USER_ID_FIELD);
            name = (String) document.get(USER_NAME_FIELD);
            if (document.get(USER_PICTURE_FIELD) != null) {
                picture = Uri.parse((String) document.get(USER_PICTURE_FIELD));
            }
            evals = (ArrayList<String>) document.get(USER_EVALUATIONS_FIELD);
            lunchChoice = (String) document.get(USER_LUNCH_CHOICE_FIELD);
        }
        return new UserEntity(id, name, picture, lunchChoice, evals);
    }

    @Override
    public void usersData(ArrayList<Map<String, Object>> userDocuments, DetailCallback callback) {
        ArrayList<UserEntity> users = new ArrayList<>();
        for (Map<String, Object> document : userDocuments) {
            users.add(getUserEntityFromDocument(document));
        }
        callback.userCallback(users);
    }

    @Override
    public void usersAndRestaurantsData(ArrayList<Map<String, Object>> userDocuments, ArrayList<Map<String, Object>> restaurantDocuments, CoworkerCallback callback) {
        ArrayList<UserEntity> users = new ArrayList<>();
        ArrayList<RestaurantEntity> restaurants = new ArrayList<>();
        for (Map<String, Object> restaurantDocument : restaurantDocuments) {
            restaurants.add(getRestaurantFromDocument(restaurantDocument));
        }
        for (Map<String, Object> userDocument : userDocuments) {
            users.add(getUserEntityFromDocument(userDocument));
        }
        callback.coworkerCallback(users, restaurants);
    }
}
