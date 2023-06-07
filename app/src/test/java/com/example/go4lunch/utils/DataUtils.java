package com.example.go4lunch.utils;

import static org.mockito.Mockito.mock;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataUtils {
    static private final LatLng userPos = new LatLng(0.0, 0.0);
    static private final Uri mockUri = mock(Uri.class);
    static private final ArrayList<String> eval = new ArrayList<>(Arrays.asList("lunch_choice", "lunch_two", "lunch_three"));
    static private final UserEntity userDefault = new UserEntity("uid",
            "user_name",
            mockUri,
            "lunch_choice",
            eval);
    static private final ArrayList<String> lunchers = new ArrayList<>(Arrays.asList("uid", "lunch_two", "lunch_three"));
    static private final ArrayList<String> lunchersTwo = new ArrayList<>(Collections.singletonList("lunch_three"));
    static private final ArrayList<String> lunchersThree = new ArrayList<>(Arrays.asList("lunch_two", "lunch_three"));
    static private final RestaurantEntity restaurantDefault = new RestaurantEntity("lunch_choice",
            "restA",
            "description",
            "openingHour",
            new GeoPoint(0.0009, 0.0009),
            0L,
            "url",
            lunchers);
    static private final RestaurantEntity restaurantDefaultTwo = new RestaurantEntity("lunch_two",
            "restB",
            "description",
            "openingHour",
            new GeoPoint(0.0, 0.0),
            2L,
            "url",
            lunchersTwo);
    static private final RestaurantEntity restaurantDefaultThree = new RestaurantEntity("lunch_three",
            "restC",
            "description",
            "openingHour",
            new GeoPoint(0.0001, 0.0001),
            4L,
            "url",
            lunchersThree);

    @NonNull
    public static UserEntity getDefaultCurrentUser() {
        return userDefault;
    }

    @NonNull
    public static String getDefaultCurrentUserMail() {
        return "default_mail@gmail.com";
    }

    @NonNull
    public static String getDefaultCurrentUserLuncher() {
        return "restaurantId";
    }

    public static RestaurantEntity getDefaultCurrentUserRestaurant() {
        return restaurantDefault;
    }

    public static LatLng getDefaultCurrentUserPosition() {
        return userPos;
    }

    public static List<RestaurantEntity> getDefaultRestaurantsByPosition() {
        return new ArrayList<>(Arrays.asList(restaurantDefaultTwo, restaurantDefaultThree, restaurantDefault));
    }

    public static List<RestaurantEntity> getDefaultRestaurantsByEval() {
        return new ArrayList<>(Arrays.asList(restaurantDefaultThree, restaurantDefaultTwo, restaurantDefault));
    }

    public static List<RestaurantEntity> getDefaultRestaurantsByLuncher() {
        return new ArrayList<>(Arrays.asList(restaurantDefaultTwo, restaurantDefaultThree, restaurantDefault));
    }
}
