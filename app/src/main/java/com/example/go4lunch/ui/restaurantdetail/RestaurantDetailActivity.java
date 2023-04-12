package com.example.go4lunch.ui.restaurantdetail;

import static android.Manifest.permission.CALL_PHONE;
import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
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

public class RestaurantDetailActivity extends AppCompatActivity {
    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailViewModel viewModel;
    private RestaurantEntity restaurant;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        IntentHelper helper = new IntentHelper();
        restaurant = helper.getRestaurantClicked(getIntent());
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RestaurantDetailViewModel.class);
        viewModel.state.observe(this, this::render);
        viewModel.initDetail(restaurant.getRestaurantid());
    }

    private void render(RestaurantDetailState restaurantDetailState) {
        if(restaurantDetailState instanceof HasEvaluate){
            HasEvaluate state = (HasEvaluate) restaurantDetailState;
            if(state.isEvaluate()){
                binding.starButton.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.baseline_star_24));
            } else {
                binding.starButton.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.baseline_star_outline_24_orange));
            }
            viewModel.initLunchers();
        }
        if(restaurantDetailState instanceof CurrentUserLunchState) {
            CurrentUserLunchState state = (CurrentUserLunchState) restaurantDetailState;
            if(state.isCurrentUserLuncher()){
                binding.lunchButton.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.baseline_check_24));
            } else {
                binding.lunchButton.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.baseline_restaurant_24));
            }
        }
        if(restaurantDetailState instanceof LuncherState) {
            LuncherState state = (LuncherState) restaurantDetailState;
            RestaurantDetailUserRecyclerViewAdapter adapter = new RestaurantDetailUserRecyclerViewAdapter(
                    state.getUserEntities(),
                    this);
            binding.lunchersRecycler.setAdapter(adapter);

        }
        if(restaurantDetailState instanceof WithResponseState) {
            WithResponseState state = (WithResponseState) restaurantDetailState;
            viewModel.initEvaluation();
            requestPermissions(new String[]{CALL_PHONE}, 1);
            String picUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference=" + restaurant.getDrawableUrl() + "&key=" + MAPS_API_KEY;
            Glide.with(this)
                    .load(picUrl)
                    .placeholder(R.drawable.sharp_image_24)
                    .into(binding.restaurantDetailPicture);
            binding.restaurantDetailName.setText(restaurant.getRestaurantname());
            binding.restaurantDetailDescription.setText(restaurant.getRestaurantdescription());
            if(restaurant.getEvaluation() != null){
                int starNumber = (int) (restaurant.getEvaluation()/3);
                for(int i = 0; i<starNumber; i++) {
                    if(i<=2) {
                        ImageView imageView = new ImageView(this);
                        imageView.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.baseline_star_outline_24));
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
                    ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("phone number",state.getRestaurantDetailEntity().getFormattedPhoneNumber());
                    clipboard.setPrimaryClip(clip);
                }
            });
            binding.websiteButton.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(state.getRestaurantDetailEntity().getWebsiteUrl()))));
            binding.starButton.setOnClickListener(view -> viewModel.addOrRemoveEvaluation());
        }
    }
}
