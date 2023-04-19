package com.example.go4lunch.ui;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityRestaurantBinding;
import com.example.go4lunch.ui.coworker.CoworkerFragment;
import com.example.go4lunch.ui.login.LoginActivity;
import com.example.go4lunch.ui.restaurantlist.RestaurantListFragment;
import com.example.go4lunch.ui.restaurantmap.RestaurantMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RestaurantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityRestaurantBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private AppBarConfiguration mAppBarConfiguration;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(),
                    result -> {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, new RestaurantMapFragment());
                        ft.commit();
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                R.string.nav_open,
                R.string.nav_close);
        binding.drawerView.getHeaderView(0);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.drawerView.setNavigationItemSelectedListener(this);


        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);
                MenuItem menuItem = menu.findItem(R.id.menu_search);
                androidx.appcompat.widget.SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setQueryHint(getString(R.string.search_hint));
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return true;
            }
        });
        setListeners();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}
            );
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new RestaurantMapFragment());
            ft.commit();
        }
    }

    private void setListeners() {
        binding.navBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mapButton:
                    openFragment(new RestaurantMapFragment());
                    return true;
                case R.id.listButton:
                    openFragment(new RestaurantListFragment());
                    return true;
                case R.id.coworkerButton:
                    openFragment(new CoworkerFragment());
                    return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment newFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, newFragment);
        ft.commit();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
                return true;
            case R.id.nav_my_lunch:
                return true;
            case R.id.nav_settings:
                return true;
        }
        return false;
    }
}
