package com.example.go4lunch.ui.login;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.data.UserRepositoryContract;
import com.example.go4lunch.model.UserEntity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.data.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final UserRepositoryContract userRepository;
    private final MutableLiveData<LoginViewState> _state = new MutableLiveData<>();
    final LiveData<LoginViewState> state = _state;

    public LoginViewModel(UserRepositoryContract userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressLint("RestrictedApi")
    void onSignInResult(FirebaseAuthUIAuthenticationResult result){
       if(Objects.requireNonNull(result.getIdpResponse()).isSuccessful()){
           User user = result.getIdpResponse().getUser();
           this._state.postValue(new OnSignInSuccess(new UserEntity(user.getProviderId(),
                   user.getName(),
                   user.getPhotoUri(),
                   "",
                   new ArrayList<>())));
       }
       else {
           this._state.postValue(new OnSignInFailure());
       }
    }

    public void createUser() {
        userRepository.createUser();
    }

    public void initUi() {
        if (userRepository.getCurrentUser() != null) {
            this._state.postValue(new UserAlreadySignIn(true));
        } else {
            this._state.postValue(new UserAlreadySignIn(false));
        }
    }
}
