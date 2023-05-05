package com.example.go4lunch.ui.restaurantdetail;

import static android.Manifest.permission.CALL_PHONE;
import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.example.go4lunch.utils.IntentHelper.RESTAURANT_CLICKED;
import static com.example.go4lunch.utils.NotificationLunch.channelID;
import static com.example.go4lunch.utils.NotificationLunch.messageExtra;
import static com.example.go4lunch.utils.NotificationLunch.notificationID;
import static com.example.go4lunch.utils.NotificationLunch.titleExtra;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModelFactory;
import com.example.go4lunch.adapter.RestaurantDetailUserRecyclerViewAdapter;
import com.example.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.IntentHelper;
import com.example.go4lunch.utils.NotificationLunch;
import com.google.gson.Gson;

import java.util.Calendar;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailViewModel viewModel;
    private RestaurantEntity restaurant;
    public static String SHARED_PREFERENCES_RESTAURANT_ID = "RESTAURANT_ID";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //restaurant pas récupérer lors de la notification si l'appli est kill
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        IntentHelper helper = new IntentHelper();
        restaurant = helper.getRestaurantClicked(getIntent());
        SharedPreferences preferences =  this.getSharedPreferences(SHARED_PREFERENCES_RESTAURANT_ID, Context.MODE_PRIVATE);
        String restaurantJson = preferences.getString(SHARED_PREFERENCES_RESTAURANT_ID, null);
        Gson gson = new Gson();
        restaurant = restaurant!=null ? restaurant : gson.fromJson(restaurantJson, RestaurantEntity.class);
        createNotificationChannel();
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantDetailViewModel.class);
        viewModel.state.observe(this, this::render);
        viewModel.initDetail(restaurant.getRestaurantid());
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = getString(R.string.notification_name);
            String desc = getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification() {
        Intent intent = new Intent(getApplicationContext(), NotificationLunch.class);
        String title = getString(R.string.notification_title);
        String message = getString(R.string.notification_message_pre)
                + restaurant.getRestaurantname()
                + getString(R.string.notification_message_post);
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);

        intent.putExtra(RESTAURANT_CLICKED, restaurant);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Long time = getLunchTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        SharedPreferences preferences = this.getSharedPreferences(SHARED_PREFERENCES_RESTAURANT_ID, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        preferences.edit()
                .putString(SHARED_PREFERENCES_RESTAURANT_ID, json)
                .apply();
    }

    private Long getLunchTime() {
        long timeStamp;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = 12;
        int minute = 0;
        calendar.set(year, month, day, hour, minute);
        timeStamp = calendar.getTimeInMillis();
        return timeStamp;
    }

    private void render(RestaurantDetailState restaurantDetailState) {
        if (restaurantDetailState instanceof HasEvaluate) {
            HasEvaluate state = (HasEvaluate) restaurantDetailState;
            renderHasEvaluate(state);
        }
        if (restaurantDetailState instanceof CurrentUserLunchState) {
            CurrentUserLunchState state = (CurrentUserLunchState) restaurantDetailState;
            renderCurrentLuncher(state);
        }
        if (restaurantDetailState instanceof LuncherState) {
            LuncherState state = (LuncherState) restaurantDetailState;
            renderLunchers(state);

        }
        if (restaurantDetailState instanceof WithResponseState) {
            WithResponseState state = (WithResponseState) restaurantDetailState;
            renderWithResponse(state);
        }
    }

    private void renderWithResponse(WithResponseState state) {
        viewModel.initEvaluation();
        requestPermissions(new String[]{CALL_PHONE}, 1);
        String picUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference=" + restaurant.getDrawableUrl() + "&key=" + MAPS_API_KEY;
        Glide.with(this)
                .load(picUrl)
                .placeholder(R.drawable.sharp_image_24)
                .into(binding.restaurantDetailPicture);
        binding.restaurantDetailName.setText(restaurant.getRestaurantname());
        binding.restaurantDetailDescription.setText(restaurant.getRestaurantdescription());
        if (restaurant.getEvaluation() != null) {
            int starNumber = (int) (restaurant.getEvaluation() / 3);
            for (int i = 0; i < starNumber; i++) {
                if (i <= 2) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_star_outline_24));
                    binding.starNotation.addView(imageView);
                }
            }
        }
        binding.lunchButton.setOnClickListener(view -> viewModel.selectOrCancelLunch());
        binding.phoneButton.setOnClickListener(view -> {
            String number = ("tel:" + state.getRestaurantDetailEntity().getFormattedPhoneNumber());
            Intent mIntent = new Intent(Intent.ACTION_CALL);
            mIntent.setData(Uri.parse(number));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(mIntent);
            } else {
                Toast toast = new Toast(this);
                toast.setText("Le numéro de téléphone est copié dans le presse-papier");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("phone number", state.getRestaurantDetailEntity().getFormattedPhoneNumber());
                clipboard.setPrimaryClip(clip);
            }
        });
        binding.websiteButton.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(state.getRestaurantDetailEntity().getWebsiteUrl()))));
        binding.starButton.setOnClickListener(view -> viewModel.addOrRemoveEvaluation());
    }

    private void renderLunchers(LuncherState state) {
        RestaurantDetailUserRecyclerViewAdapter adapter = new RestaurantDetailUserRecyclerViewAdapter(
                state.getUserEntities(),
                this);
        binding.lunchersRecycler.setAdapter(adapter);
    }

    private void renderCurrentLuncher(CurrentUserLunchState state) {
        if (state.isCurrentUserLuncher()) {
            binding.lunchButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_check_24));
            scheduleNotification();
        } else {
            binding.lunchButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_restaurant_24));
            SharedPreferences preferences = this.getSharedPreferences(SHARED_PREFERENCES_RESTAURANT_ID, Context.MODE_PRIVATE);
            preferences.edit()
                    .remove(SHARED_PREFERENCES_RESTAURANT_ID)
                    .apply();
        }
    }

    private void renderHasEvaluate(HasEvaluate state) {
        if (state.isEvaluate()) {
            binding.starButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_star_24));
        } else {
            binding.starButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_star_outline_24_orange));
        }
        viewModel.initLunchers();
    }
}
