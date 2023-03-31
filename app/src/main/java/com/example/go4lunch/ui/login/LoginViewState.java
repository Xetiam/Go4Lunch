package com.example.go4lunch.ui.login;

import com.example.go4lunch.model.UserEntity;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewState {}

class OnSignInSuccess extends LoginViewState {
    private final UserEntity user;

    public OnSignInSuccess(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }
}
class OnSignInFailure extends LoginViewState {}
class UserAlreadySignIn extends  LoginViewState {
    private final boolean isConnected;

    public UserAlreadySignIn(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }
}