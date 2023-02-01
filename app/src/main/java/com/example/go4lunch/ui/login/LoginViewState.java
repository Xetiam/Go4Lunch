package com.example.go4lunch.ui.login;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewState {}

class OnSignInSuccess extends LoginViewState {
    private final User user;

    public OnSignInSuccess(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
class OnSignInFailure extends LoginViewState {}
