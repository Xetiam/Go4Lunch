package com.example.go4lunch.ui.settings;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.UserRepository;
import com.example.go4lunch.data.UserRepositoryContract;
import com.example.go4lunch.utils.SignOutCallback;

import java.util.Objects;

public class SettingsViewModel extends ViewModel implements SignOutCallback {
    private final UserRepositoryContract userRepository;
    private final MutableLiveData<SettingsState> _state = new MutableLiveData<>();
    LiveData<SettingsState> state = _state;

    public SettingsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void suppressAccount(Context context) {
        userRepository.deleteUserFromFirestore(this);
    }

    public void modifyUserName(String newUserName) {
        if (!Objects.equals(newUserName, "")) {
            userRepository.updateUsername(newUserName);
        } else {
            _state.setValue(new NoInputState());
        }
    }

    @Override
    public void signout() {
        _state.setValue(new HasSignOutAndDelete());
    }
}
