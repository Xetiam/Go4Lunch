package com.example.go4lunch.ui.restaurantlist;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.databinding.FragmentRestaurantListBinding;

public class RestaurantListFragment extends Fragment {
    private FragmentRestaurantListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantListBinding.inflate(inflater);
        View view = binding.getRoot();
        return view;
    }
}
