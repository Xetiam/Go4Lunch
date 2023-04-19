package com.example.go4lunch.ui.restaurantmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.databinding.FragmentRestaurantMapBinding;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.IntentHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantMapFragment extends Fragment
        implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private RestaurantMapViewModel viewModel;
    private List<RestaurantEntity> restaurants = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRestaurantMapBinding binding = FragmentRestaurantMapBinding.inflate(inflater);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                MenuItem menuItem = menu.findItem(R.id.menu_search);
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        viewModel.search(s);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        viewModel.search(s);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_view);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantMapViewModel.class);
        viewModel.state.observe(getViewLifecycleOwner(), this::render);
        Objects.requireNonNull(supportMapFragment).getMapAsync(this);
        return binding.getRoot();
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void render(RestaurantMapViewState restaurantMapViewState) {
        if (restaurantMapViewState instanceof WithResponseState) {
            WithResponseState state = (WithResponseState) restaurantMapViewState;
            restaurants = state.getRestaurants();
            googleMap.clear();
            if (restaurants != null) {
                for (RestaurantEntity restaurant : restaurants) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(restaurant.getRestaurantposition())
                            .title(restaurant.getRestaurantname()));
                }
            }
            googleMap.setOnMarkerClickListener(marker -> {
                for (RestaurantEntity restaurant : restaurants) {
                    if (marker.getPosition().equals(restaurant.getRestaurantposition())) {
                        IntentHelper helper = new IntentHelper();
                        helper.goToRestaurantDetail(requireActivity(), restaurant);
                    }
                }
                return true;
            });
        }
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(),
                    result -> {
                    });

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}
            );
            return;
        }
        FusedLocationProviderClient provider = LocationServices.getFusedLocationProviderClient(requireActivity());
        provider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }

                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                })
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        googleMap.clear();
                        googleMap.setMyLocationEnabled(true);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15F));
                        googleMap.getUiSettings().setCompassEnabled(true);
                        viewModel.getResponseLiveData(userPosition);
                    }
                });
    }
}
