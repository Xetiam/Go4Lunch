package com.example.go4lunch.ui.restaurantmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.databinding.FragmentRestaurantMapBinding;
import com.google.android.gms.maps.SupportMapFragment;


public class RestaurantMapFragment extends SupportMapFragment {

    private RestaurantMapViewModel viewModel;
    private FragmentRestaurantMapBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantMapBinding.inflate(inflater);
        return binding.getRoot();
    }
}
