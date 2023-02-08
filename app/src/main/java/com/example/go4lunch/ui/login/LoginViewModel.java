package com.example.go4lunch.ui.login;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginViewState> _state = new MutableLiveData<>();
    final LiveData<LoginViewState> state = _state;

    @SuppressLint("RestrictedApi")
    void onSignInResult(FirebaseAuthUIAuthenticationResult result){
       if(Objects.requireNonNull(result.getIdpResponse()).isSuccessful()){
           this._state.postValue(new OnSignInSuccess(result.getIdpResponse().getUser()));
       }
       else {
           this._state.postValue(new OnSignInFailure());
       }
    }
}
