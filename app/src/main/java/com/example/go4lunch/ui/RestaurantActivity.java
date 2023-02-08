package com.example.go4lunch.ui;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.databinding.ActivityRestaurantBinding;
import com.example.go4lunch.ui.coworker.CoworkerFragment;
import com.example.go4lunch.ui.login.LoginViewState;
import com.example.go4lunch.ui.restaurantlist.RestaurantListFragment;
import com.example.go4lunch.ui.restaurantmap.RestaurantMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityRestaurantBinding binding;
    private RestaurantViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantViewModel.class);
        locationPermissionRequest.launch(new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }
        );
        setListeners();
        viewModel.state.observe(this, this::render);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void render(LoginViewState loginViewState) {
    }

    private void setListeners() {
        FragmentManager fm = getSupportFragmentManager();
        binding.mapButton.setOnClickListener(view -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.map, new RestaurantMapFragment());
            ft.commit();
        });
        binding.listButton.setOnClickListener(view -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(binding.fragmentContainer.getId(), new RestaurantListFragment());
            ft.commit();
        });
        binding.coworkerButton.setOnClickListener(view -> {
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
                        viewModel.initPosition(fineLocationGranted, coarseLocationGranted);
                    }
            );

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
