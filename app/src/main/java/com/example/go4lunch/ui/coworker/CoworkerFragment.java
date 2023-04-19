package com.example.go4lunch.ui.coworker;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.adapter.CoworkerRecyclerViewAdapter;
import com.example.go4lunch.databinding.FragmentCoworkerBinding;

public class CoworkerFragment extends Fragment {
    private FragmentCoworkerBinding binding;
    private CoworkerViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoworkerBinding.inflate(inflater);
        View view = binding.getRoot();

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

        binding.coworkerRecycler.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(CoworkerViewModel.class);
        viewModel.state.observe(requireActivity(), this::render);
        viewModel.initCoworker();
        return view;
    }

    private void render(CoworkerViewState coworkerViewState) {
        if (coworkerViewState instanceof UserResponseState) {
            UserResponseState state = (UserResponseState) coworkerViewState;
            CoworkerRecyclerViewAdapter adapter = new CoworkerRecyclerViewAdapter(
                    state.getUserEntities(),
                    state.getRestaurantEntities(),
                    requireActivity());
            binding.coworkerRecycler.setAdapter(adapter);
        }
    }
}
