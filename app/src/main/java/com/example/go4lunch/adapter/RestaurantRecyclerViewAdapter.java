package com.example.go4lunch.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.databinding.ItemRestaurantRecyclerViewBinding;
import com.example.go4lunch.model.RestaurantEntity;

import java.util.ArrayList;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {
    public ArrayList<RestaurantEntity> restaurants;

    public RestaurantRecyclerViewAdapter(ArrayList<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
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
        holder.binding.restaurantName.setText(restaurantEntity.getName());
        holder.binding.restaurantDescription.setText(restaurantEntity.getDescription());
        //holder.binding.restaurantDistance.setText(stringDistanceFormatter(restaurantEntity.getPosition()));
        //holder.binding.restaurantOpeningTime.setText(stringOpeningTimeFormatter(restaurantEntity.getOpeningTime()));
        //holder.binding.starNotation.
        String stringReco = "("+restaurantEntity.getCoworkerStars().size()+")";
        holder.binding.restaurantRecommandation.setText(stringReco);
        //holder.binding.restaurantPicture.setImageDrawable(restaurantEntity.getDrawableUrl());
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