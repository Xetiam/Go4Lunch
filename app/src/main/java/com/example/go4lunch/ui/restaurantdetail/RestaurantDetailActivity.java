package com.example.go4lunch.ui.restaurantdetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.IntentHelper;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ActivityRestaurantDetailBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        IntentHelper helper = new IntentHelper();
        RestaurantEntity restaurant = helper.getRestaurantClicked(getIntent());
        binding.restaurantName.setText(restaurant.getRestaurantname());
    }
}
