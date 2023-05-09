package com.example.go4lunch.ui.restaurantlist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.adapter.RestaurantRecyclerViewAdapter;
import com.example.go4lunch.databinding.FragmentRestaurantListBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RestaurantListFragment extends Fragment {
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(),
                    result -> {
                    });
    private FragmentRestaurantListBinding binding;
    private RestaurantsListViewModel viewModel;
    private LatLng userPosition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantListBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.restaurantRecycler.setHasFixedSize(false);
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
        binding.restaurantRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.restaurantRecycler.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantsListViewModel.class);
        viewModel.state.observe(requireActivity(), this::render);
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}
            );
        }
        FusedLocationProviderClient provider = LocationServices.getFusedLocationProviderClient(requireActivity());
        provider.getLastLocation().addOnSuccessListener(location -> {
            userPosition = new LatLng(location.getLatitude(), location.getLongitude());
            viewModel.initRestaurantList(userPosition);
        });
        setSorterSpinner();
        return view;
    }

    private void setSorterSpinner() {
        ArrayList<String> arraySorter = new ArrayList<>();
        arraySorter.add(getString(R.string.spinner_by_position));
        arraySorter.add(getString(R.string.spinner_by_evaluation));
        arraySorter.add(getString(R.string.spinner_by_luncher));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, arraySorter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(adapter);
        binding.spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.sortRestaurants(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void render(RestaurantListViewState restaurantListViewState) {
        if (restaurantListViewState instanceof WithResponseState) {
            WithResponseState state = (WithResponseState) restaurantListViewState;
            RestaurantRecyclerViewAdapter adapter = new RestaurantRecyclerViewAdapter(
                    state.getRestaurants(),
                    userPosition,
                    requireActivity());
            binding.restaurantRecycler.setAdapter(adapter);
            binding.restaurantRecycler.getAdapter().notifyDataSetChanged();
        }
    }
}
