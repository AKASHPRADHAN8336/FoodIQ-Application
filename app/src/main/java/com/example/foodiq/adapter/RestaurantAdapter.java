package com.example.foodiq.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiq.HomeActivity;
import com.example.foodiq.R;
import com.example.foodiq.model.restaurant;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<restaurant> restaurantList;
    private static final String TAG = "RestaurantAdapter";

    public RestaurantAdapter(HomeActivity homeActivity, List<restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        Log.d(TAG, "Adapter created with " + (restaurantList != null ? restaurantList.size() : 0) + " items");
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurantsdetails, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder position: " + position);

        if (restaurantList == null || restaurantList.isEmpty()) {
            Log.e(TAG, "restaurantList is null or empty at position: " + position);
            return;
        }

        restaurant restaurant = restaurantList.get(position);
        Log.d(TAG, "Binding restaurant: " + restaurant.getRestaurantName());

        // Set data with null checks
        if (holder.txtRestaurantName != null) {
            holder.txtRestaurantName.setText(restaurant.getRestaurantName() != null ? restaurant.getRestaurantName() : "Unknown Restaurant");
        } else {
            Log.e(TAG, "txtRestaurantName is NULL - this will cause crash!");
        }

        if (holder.txtRating != null) {
            holder.txtRating.setText("4.5"); // Default rating since your model doesn't have rating
        }

        if (holder.txtCuisineType != null) {
            holder.txtCuisineType.setText("Multi Cuisine"); // Default cuisine type
        }

        if (holder.txtDistance != null) {
            holder.txtDistance.setText("2.5 km");
        }

        if (holder.txtTiming != null) {
            holder.txtTiming.setText(restaurant.getOpeneingTime() != null ? restaurant.getOpeneingTime() : "10 AM - 11 PM");
        }

        if (holder.txtCost != null) {
            holder.txtCost.setText(restaurant.getPrice() != null ? restaurant.getPrice() : "₹500 for two");
        }

        // SETUP IMAGE ADAPTER FOR THE RESTAURANT IMAGES
        setupImageGallery(holder, restaurant);
    }

    private void setupImageGallery(RestaurantViewHolder holder, restaurant restaurant) {
        if (holder.recyclerImages == null) {
            Log.e(TAG, "recyclerImages is null - cannot setup image gallery");
            return;
        }

        // Get image URLs from the Map
        List<String> imageUrls = restaurant.getImageUrlAsList();

        Log.d(TAG, "Found " + imageUrls.size() + " images for restaurant: " + restaurant.getRestaurantName());

        // Setup the ImageAdapter
        ImageAdapter imageAdapter = new ImageAdapter(holder.itemView.getContext(), imageUrls);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerImages.setLayoutManager(layoutManager);
        holder.recyclerImages.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView txtRestaurantName, txtRating, txtCuisineType, txtDistance, txtTiming, txtCost;
        RecyclerView recyclerImages;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("RestaurantViewHolder", "ViewHolder constructor called");

            // Initialize all views
            txtRestaurantName = itemView.findViewById(R.id.txtRestaurantName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtCuisineType = itemView.findViewById(R.id.txtCuisineType);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtTiming = itemView.findViewById(R.id.txtTiming);
            txtCost = itemView.findViewById(R.id.txtCost);
            recyclerImages = itemView.findViewById(R.id.recyclerImages);

            // Log which views are found
            Log.d("RestaurantViewHolder", "txtRestaurantName found: " + (txtRestaurantName != null));
            Log.d("RestaurantViewHolder", "txtRating found: " + (txtRating != null));
            Log.d("RestaurantViewHolder", "txtCuisineType found: " + (txtCuisineType != null));
            Log.d("RestaurantViewHolder", "txtDistance found: " + (txtDistance != null));
            Log.d("RestaurantViewHolder", "txtTiming found: " + (txtTiming != null));
            Log.d("RestaurantViewHolder", "txtCost found: " + (txtCost != null));
            Log.d("RestaurantViewHolder", "recyclerImages found: " + (recyclerImages != null));
        }
    }

    public void updateList(List<restaurant> newList) {
        this.restaurantList = newList;
        notifyDataSetChanged();
    }
}