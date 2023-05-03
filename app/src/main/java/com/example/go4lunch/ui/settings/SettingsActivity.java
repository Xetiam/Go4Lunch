package com.example.go4lunch.ui.settings;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.ui.login.LoginActivity;

public class SettingsActivity  extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;
    private final String NOTIFICATIONS_PREFERENCES = "notification_preferences";
    private final String IS_ACTIVATED = "is_activated";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = ViewModelFactory.getInstance().create(SettingsViewModel.class);
        viewModel.state.observe(this, this::render);
        setListeners();
        super.onCreate(savedInstanceState);
    }

    private void render(SettingsState settingsState) {
        if(settingsState instanceof NoInputState){
            Toast toast = new Toast(this);
            toast.setText(R.string.toast_modify_user_name);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }
        if(settingsState instanceof HasSignOutAndDelete) {
            goToLoginActivity();
        }
    }

    private void setListeners() {
        SharedPreferences prefs = this.getSharedPreferences(NOTIFICATIONS_PREFERENCES, Context.MODE_PRIVATE);
        binding.notificationSwitch.setChecked(prefs.getBoolean(IS_ACTIVATED, true));
        binding.notificationSwitch.setOnClickListener(view -> {
            prefs.edit()
                    .putBoolean(IS_ACTIVATED, binding.notificationSwitch.isChecked())
                    .apply();
        });
        binding.deleteAccountButton.setOnClickListener(view -> {
            viewModel.suppressAccount(this);
        });
        binding.validateUserNameButton.setOnClickListener(view -> {
            viewModel.modifyUserName(binding.userNameModifier.getText().toString());
            View keyBoard = this.getCurrentFocus();
            if (keyBoard != null) {
                binding.userNameModifier.setText("");
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(intent);
    }

}
