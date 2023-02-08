package com.example.go4lunch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.ui.login.LoginViewState;

public class RestaurantViewModel extends ViewModel {
    private final MutableLiveData<LoginViewState> _state = new MutableLiveData<>();
    final LiveData<LoginViewState> state = _state;


    public void initPosition(Boolean fineLocationGranted, Boolean coarseLocationGranted) {
        if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
            /*
            FragmentManager fm = this.getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(binding.fragmentContainer.getId(), new RestaurantMapFragment());
            ft.commit();*/
        } else {
            //finish();
        }
    }
}
