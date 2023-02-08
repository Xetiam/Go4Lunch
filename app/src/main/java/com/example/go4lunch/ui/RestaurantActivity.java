package com.example.go4lunch.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivityRestaurantBinding;
import com.example.go4lunch.ui.coworker.CoworkerFragment;
import com.example.go4lunch.ui.restaurantlist.RestaurantListFragment;
import com.example.go4lunch.ui.restaurantmap.RestaurantMapFragment;

public class RestaurantActivity extends AppCompatActivity {
    private ActivityRestaurantBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        locationPermissionRequest.launch(new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }
        );
        setListeners();
    }

    private void setListeners() {
        binding.mapButton.setOnClickListener(view -> {
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(binding.fragmentContainer.getId(), new RestaurantMapFragment());
            ft.commit();
        });
        binding.listButton.setOnClickListener(view -> {
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(binding.fragmentContainer.getId(), new RestaurantListFragment());
            ft.commit();
        });
        binding.coworkerButton.setOnClickListener(view -> {
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(binding.fragmentContainer.getId(), new CoworkerFragment());
            ft.commit();
        });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && (fineLocationGranted || coarseLocationGranted)) {
                            FragmentManager fm = this.getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(binding.fragmentContainer.getId(), new RestaurantMapFragment());
                            ft.commit();
                        } else {
                            finish();
                        }
                    }
            );
}
