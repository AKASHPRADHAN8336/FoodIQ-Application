package com.example.foodiq.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiq.HomeActivity;
import com.example.foodiq.R;
import com.example.foodiq.restaurants_home_page;
import com.example.foodiq.model.restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<restaurant> restaurantList;
    private Context context;
    private static final String TAG = "RestaurantAdapter";

    public RestaurantAdapter(HomeActivity homeActivity, List<restaurant> restaurantList) {
        this.context = homeActivity;
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
            holder.txtRating.setText("4.5");
        }

        if (holder.txtCuisineType != null) {
            holder.txtCuisineType.setText("Multi Cuisine");
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

        // ADD CLICK LISTENER TO THE CARD VIEW
        if (holder.cardRestaurant != null) {
            holder.cardRestaurant.setOnClickListener(v -> {
                Log.d(TAG, "🎯 CARD CLICKED: " + restaurant.getRestaurantName());
                Toast.makeText(context, "Opening: " + restaurant.getRestaurantName(), Toast.LENGTH_SHORT).show();
                navigateToRestaurant(restaurant);
            });
        } else {
            // Fallback to itemView if cardRestaurant is null
            Log.e(TAG, "cardRestaurant is null, using itemView as fallback");
            holder.itemView.setOnClickListener(v -> {
                Log.d(TAG, "🎯 ITEM VIEW CLICKED: " + restaurant.getRestaurantName());
                Toast.makeText(context, "Opening: " + restaurant.getRestaurantName(), Toast.LENGTH_SHORT).show();
                navigateToRestaurant(restaurant);
            });
        }

        // Also make sure the item view is clickable
        holder.itemView.setClickable(true);
        holder.itemView.setFocusable(true);
    }

    private void navigateToRestaurant(restaurant restaurant) {
        Log.d(TAG, "🚀 NAVIGATING TO: " + restaurant.getRestaurantName());

        try {
            Intent intent = new Intent(context, restaurants_home_page.class);

            // Pass ALL restaurant data to the next activity
            intent.putExtra("RESTAURANT_ID", restaurant.getId());
            intent.putExtra("RESTAURANT_NAME", restaurant.getRestaurantName());
            intent.putExtra("RESTAURANT_PRICE", restaurant.getPrice());
            intent.putExtra("RESTAURANT_OPEN_TIME", restaurant.getOpeneingTime());
            intent.putExtra("RESTAURANT_CLOSE_TIME", restaurant.getClosingTime());

            // Pass the new fields
            intent.putExtra("RESTAURANT_CUISINE_TYPE", restaurant.getCuisineType());
            intent.putExtra("RESTAURANT_DELIVERY_TIME", restaurant.getDeliveryTime());
            intent.putExtra("RESTAURANT_RATING", restaurant.getRating());
            intent.putExtra("RESTAURANT_ADDRESS", restaurant.getAddress());
            intent.putExtra("RESTAURANT_PHONE", restaurant.getPhone());
            intent.putExtra("RESTAURANT_WEBSITE", restaurant.getWebsite());
            intent.putExtra("RESTAURANT_DISTANCE", restaurant.getDistance());

            Log.d(TAG, "Intent created with complete restaurant data");
            Log.d(TAG, "Target activity: " + restaurants_home_page.class.getName());

            // Start the activity
            context.startActivity(intent);
            Log.d(TAG, "startActivity called successfully");

        } catch (Exception e) {
            Log.e(TAG, "ERROR starting activity: " + e.getMessage(), e);
            Toast.makeText(context, "Error opening restaurant: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
        com.google.android.material.card.MaterialCardView cardRestaurant; // Add this line

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("RestaurantViewHolder", "ViewHolder constructor called");

            // Initialize all views - ADD cardRestaurant
            cardRestaurant = itemView.findViewById(R.id.cardRestaurant); // Add this line
            txtRestaurantName = itemView.findViewById(R.id.txtRestaurantName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtCuisineType = itemView.findViewById(R.id.txtCuisineType);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtTiming = itemView.findViewById(R.id.txtTiming);
            txtCost = itemView.findViewById(R.id.txtCost);
            recyclerImages = itemView.findViewById(R.id.recyclerImages);

            // Log which views are found
            Log.d("RestaurantViewHolder", "cardRestaurant found: " + (cardRestaurant != null)); // Add this line
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