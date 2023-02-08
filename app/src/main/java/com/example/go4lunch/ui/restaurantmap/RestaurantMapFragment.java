package com.example.go4lunch.ui.restaurantmap;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.go4lunch.databinding.FragmentRestaurantMapBinding;


public class RestaurantMapFragment extends Fragment {

    private RestaurantMapViewModel viewModel;
    private FragmentRestaurantMapBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantMapBinding.inflate(inflater);
        return binding.getRoot();
    }


}
