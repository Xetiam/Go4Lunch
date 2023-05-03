package com.example.go4lunch.utils;

import android.content.Context;
import android.content.Intent;

import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.ui.restaurantdetail.RestaurantDetailActivity;

public class IntentHelper {
    public final String RESTAURANT_CLICKED = "RESTAURANT_CLICKED";

    public RestaurantEntity getRestaurantClicked(Intent intent) {
        return (RestaurantEntity) intent.getSerializableExtra(RESTAURANT_CLICKED);
    }

    public void goToRestaurantDetail(Context context, RestaurantEntity restaurant) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(RESTAURANT_CLICKED, restaurant);
        context.startActivity(intent);
    }
}
