package com.example.go4lunch.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ItemCoworkerRecyclerviewBinding;
import com.example.go4lunch.model.RestaurantEntity;
import com.example.go4lunch.model.UserEntity;

import java.util.List;
import java.util.Objects;

public class CoworkerRecyclerViewAdapter extends RecyclerView.Adapter<CoworkerRecyclerViewAdapter.ViewHolder> {
    public List<UserEntity> lunchers;
    private final Context context;
    private final List<RestaurantEntity> restaurantEntities;

    public CoworkerRecyclerViewAdapter(List<UserEntity> lunchers, List<RestaurantEntity> restaurantEntities, Context context) {
        this.lunchers = lunchers;
        this.context = context;
        this.restaurantEntities = restaurantEntities;
    }


    @NonNull
    @Override
    public CoworkerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCoworkerRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkerRecyclerViewAdapter.ViewHolder holder, final int position) {
        UserEntity userEntity = lunchers.get(position);
        Glide.with(context)
                .load(userEntity.getUrlPicture())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.binding.profilePicture);
        String userDescriptionString = userEntity.getUsername() + "hasn't decided yet";
        if (!Objects.equals(userEntity.getLunchChoice(), null) || !Objects.equals(userEntity.getLunchChoice(), "")) {
            userDescriptionString = userEntity.getUsername() + " is eating (" + getRestaurantById(restaurantEntities, userEntity.getLunchChoice()) + ")";
        } else {
            holder.binding.userDescription.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        }
        holder.binding.userDescription.setText(userDescriptionString);
    }

    private String getRestaurantById(List<RestaurantEntity> restaurantEntities, String lunchChoice) {
        String restaurantName = "";
        for (RestaurantEntity restaurant : restaurantEntities) {
            if (Objects.equals(restaurant.getRestaurantid(), lunchChoice)) {
                restaurantName = restaurant.getRestaurantname();
                break;
            }
        }
        return restaurantName;
    }

    @Override
    public int getItemCount() {
        return lunchers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCoworkerRecyclerviewBinding binding;

        public ViewHolder(ItemCoworkerRecyclerviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}