package com.example.go4lunch.ui.restaurantlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.adapter.RestaurantRecyclerViewAdapter;
import com.example.go4lunch.databinding.FragmentRestaurantListBinding;

public class RestaurantListFragment extends Fragment {
    private FragmentRestaurantListBinding binding;
    private RestaurantListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantListBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.restaurantRecycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.restaurantRecycler.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantListViewModel.class);
        viewModel.state.observe(requireActivity(), this::render);
        viewModel.initRestaurantList();


        return view;
    }

    private void render(RestaurantListViewState restaurantListViewState) {
        if (restaurantListViewState instanceof WithResponseState) {
            WithResponseState state = (WithResponseState) restaurantListViewState;
            RestaurantRecyclerViewAdapter adapter = new RestaurantRecyclerViewAdapter(state.getRestaurants());
            binding.restaurantRecycler.setAdapter(adapter);
        }
    }


}
