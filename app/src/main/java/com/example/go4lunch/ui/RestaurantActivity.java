package com.example.go4lunch.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.go4lunch.R;
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
        setListeners();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new RestaurantMapFragment());
        ft.commit();
    }

    private void setListeners() {
        FragmentManager fm = getSupportFragmentManager();
        binding.mapButton.setOnClickListener(view -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new RestaurantMapFragment());
            ft.commit();
        });
        binding.listButton.setOnClickListener(view -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new RestaurantListFragment());
            ft.commit();
        });
        binding.coworkerButton.setOnClickListener(view -> {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, new CoworkerFragment());
            ft.commit();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO: les deux boutons du menu sont du même côté
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
