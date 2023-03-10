package com.example.go4lunch.ui.restaurantmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.databinding.FragmentRestaurantMapBinding;
import com.example.go4lunch.model.RestaurantEntity;
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

import java.util.List;
import java.util.Objects;

public class RestaurantMapFragment extends Fragment
        implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private RestaurantMapViewModel viewModel;
    private FragmentRestaurantMapBinding binding;
    private FusedLocationProviderClient provider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantMapBinding.inflate(inflater);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_view);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantMapViewModel.class);
        Objects.requireNonNull(supportMapFragment).getMapAsync(this);
        return binding.getRoot();
    }
    private void render(RestaurantMapViewState restaurantMapViewState) {
        if(restaurantMapViewState instanceof WithResponseState){
            WithResponseState state = (WithResponseState) restaurantMapViewState;
            List<RestaurantEntity> test = state.getRestaurants();
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
                    { Manifest.permission.ACCESS_FINE_LOCATION }
            );
            return;
        }
        provider = LocationServices.getFusedLocationProviderClient(requireActivity());
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
                        googleMap.addMarker(new MarkerOptions().position(userPosition).title("Vous êtes ici"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition,10F));
                        viewModel.getResponseLiveData(userPosition).observe(getViewLifecycleOwner(), this::render);
                    }
                });
    }
}
