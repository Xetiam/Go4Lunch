package com.example.go4lunch.ui.login;


import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthUI;
import com.example.go4lunch.R;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        //TODO: Valider ou invalider la connexion
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUI();
    }

    private void initUI() {
        List<AuthUI.IdpConfig> fbAuthProvider =
                Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build());
        List<AuthUI.IdpConfig> ggAuthProvider =
                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
        setListener(ggAuthProvider, binding.googleSigninButton);
        setListener(fbAuthProvider, binding.fbSigninButton);
    }

    private void setListener(List<AuthUI.IdpConfig> authProvider, Button signInListener) {
        signInListener.setOnClickListener(view -> {
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(authProvider)
                    .build();
            signInLauncher.launch(signInIntent);
        });
    }
}
