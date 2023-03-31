package com.example.go4lunch.ui.login;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.databinding.ActivityLoginBinding;
import com.example.go4lunch.ui.RestaurantActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;

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
        viewModel.onSignInResult(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(LoginViewModel.class);
        initUI();
        viewModel.state.observe(this, this::render);
    }

    private void render(LoginViewState loginViewState) {
        if (loginViewState instanceof OnSignInSuccess) {
            showConnexionMessage(getString(R.string.success_message));
            viewModel.createUser();
            goToRestaurantActivity();
        }
        if (loginViewState instanceof OnSignInFailure) {
            showConnexionMessage(getString(R.string.failure_message));
        }
        if(loginViewState instanceof UserAlreadySignIn){
            UserAlreadySignIn state = (UserAlreadySignIn) loginViewState;
            if(state.isConnected()){
                goToRestaurantActivity();
            } else {
                List<AuthUI.IdpConfig> ggAuthProvider =
                        Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
                setListener(ggAuthProvider, binding.googleSigninButton);
                List<AuthUI.IdpConfig> mailAuthProvider =
                        Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());
                setListener(mailAuthProvider, binding.mailSigninButton);
            }
        }
    }

    private void showConnexionMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void goToRestaurantActivity() {
        Intent intent = new Intent(this, RestaurantActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }

    private void initUI() {
        viewModel.initUi();
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
