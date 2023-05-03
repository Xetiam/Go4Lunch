package com.example.go4lunch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ItemCoworkerRecyclerviewBinding;
import com.example.go4lunch.model.UserEntity;

import java.util.List;

public class RestaurantDetailUserRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantDetailUserRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    public List<UserEntity> lunchers;

    public RestaurantDetailUserRecyclerViewAdapter(List<UserEntity> lunchers, Context context) {
        this.lunchers = lunchers;
        this.context = context;
    }


    @NonNull
    @Override
    public RestaurantDetailUserRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCoworkerRecyclerviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailUserRecyclerViewAdapter.ViewHolder holder, final int position) {
        UserEntity userEntity = lunchers.get(position);
        Glide.with(context)
                .load(userEntity.getUrlPicture())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.binding.profilePicture);
        String userDescriptionString = userEntity.getUsername() + context.getString(R.string.restaurant_detail_joining);
        holder.binding.userDescription.setText(userDescriptionString);
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