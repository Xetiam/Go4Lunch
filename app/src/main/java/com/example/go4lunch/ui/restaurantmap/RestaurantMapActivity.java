package com.example.go4lunch.ui.restaurantmap;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivityRestaurantMapBinding;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.LocationProvider;


public class RestaurantMapActivity extends AppCompatActivity {
    private MapView mapView;
    private RestaurantMapViewModel viewModel;
    private ActivityRestaurantMapBinding binding;
    private LocationComponentPlugin provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        locationPermissionRequest.launch(new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }
        );
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        mapView = binding.mapView;


                        if (fineLocationGranted != null && fineLocationGranted) {
                           mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    
                                }
                            });
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
                        } else {
                            finish();
                        }
                    }
            );
}
