package com.example.go4lunch.adapter;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;
import static com.google.maps.android.SphericalUtil.computeDistanceBetween;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ItemRestaurantRecyclerViewBinding;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.utils.IntentHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {
    public List<RestaurantEntity> restaurants;
    private final LatLng userPosition;
    private final Context context;

    public RestaurantRecyclerViewAdapter(List<RestaurantEntity> restaurants, LatLng userPosition, Context context) {
        this.restaurants = restaurants;
        this.userPosition = userPosition;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRestaurantRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerViewAdapter.ViewHolder holder, final int position) {
        RestaurantEntity restaurantEntity = restaurants.get(position);
        holder.binding.restaurantName.setText(restaurantEntity.getRestaurantname());
        holder.binding.restaurantDescription.setText(restaurantEntity.getRestaurantdescription());
        String distance = (int) computeDistanceBetween(
                userPosition,
                new LatLng(restaurantEntity.getRestaurantposition().latitude, restaurantEntity.getRestaurantposition().longitude)) + "m";
        holder.binding.restaurantDistance.setText(distance);
        holder.binding.restaurantOpeningTime.setText(restaurantEntity.getOpeningHour());
        String reco = "(0)";
        if(restaurantEntity.getEvaluation() != null){
            reco = "(" + restaurantEntity.getEvaluation().toString() + ")";
            setStarsNotation(holder, restaurantEntity.getEvaluation());
        }
        holder.binding.restaurantRecommandation.setText(reco);
        String picUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference=" + restaurantEntity.getDrawableUrl() + "&key=" + MAPS_API_KEY;
        Glide.with(context)
                .load(picUrl)
                .placeholder(R.drawable.sharp_image_24)
                .into(holder.binding.restaurantPicture);
        holder.binding.getRoot().setOnClickListener(view -> {
            IntentHelper helper = new IntentHelper();
            helper.goToRestaurantDetail(context, restaurantEntity);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setStarsNotation(@NonNull RestaurantRecyclerViewAdapter.ViewHolder holder, Long evaluation) {
        holder.binding.starNotation.removeAllViews();
        if (evaluation != null) {
            int starNumber = (int) (evaluation / 3);
            for (int i = 0; i < starNumber; i++) {
                //TODO: règle métier pour la notation
                ImageView imageView = new ImageView(context);
                imageView.setImageDrawable(context.getDrawable(R.drawable.baseline_star_outline_24));
                holder.binding.starNotation.addView(imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return restaurants == null ? 0 :
                restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRestaurantRecyclerViewBinding binding;

        public ViewHolder(ItemRestaurantRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}