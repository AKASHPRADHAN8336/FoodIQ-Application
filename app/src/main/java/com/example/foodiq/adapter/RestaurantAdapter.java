package com.example.foodiq.adapter;

<<<<<<< HEAD
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
=======
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import com.example.foodiq.HomeActivity;
import com.example.foodiq.R;
import com.example.foodiq.model.restaurant;
=======
import com.example.foodiq.R;
import com.example.foodiq.model.restaurant;
import com.example.foodiq.restaurants_home_page;

import java.util.ArrayList;
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

<<<<<<< HEAD
    private List<restaurant> restaurantList;
    private static final String TAG = "RestaurantAdapter";

    public RestaurantAdapter(HomeActivity homeActivity, List<restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        Log.d(TAG, "Adapter created with " + (restaurantList != null ? restaurantList.size() : 0) + " items");
=======
    private Context context;
    private List<restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurantsdetails, parent, false);
=======
        View view = LayoutInflater.from(context).inflate(R.layout.restaurantsdetails,parent,false);
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
<<<<<<< HEAD
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
=======
        restaurant Restaurant = restaurantList.get(position);
        holder.restaurantName.setText(Restaurant.getRestaurantName());
        holder.openTime.setText("Open " + Restaurant.getOpeneingTime());
        holder.closeTime.setText("Close " + Restaurant.getClosingTime());
        holder.costText.setText(String.format("Cost per person : " , Restaurant.getPrice()));
        if(Restaurant.getImageUrl()!= null && !Restaurant.getImageUrl().isEmpty()){
            List<String> imageUrl = new ArrayList<>(Restaurant.getImageUrl().values());
            ImageAdapter imageAdapter = new ImageAdapter(context,imageUrl);
            holder.imageRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            holder.imageRecyclerView.setAdapter(imageAdapter);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, restaurants_home_page.class);
                intent.putExtra("restaurant" , Restaurant);
                context.startActivity(intent);
            }
        });
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
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
=======

        return restaurantList != null ? restaurantList.size() : 0;

    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{
        TextView restaurantName , openTime , closeTime , costText;
        RecyclerView imageRecyclerView;
        Button directionBtn,menuBtn,callBtn;
        public RestaurantViewHolder(@NonNull View itemView) {

            super(itemView);
            restaurantName = itemView.findViewById(R.id.textView);
            openTime = itemView.findViewById(R.id.openTimeText);
            closeTime = itemView.findViewById(R.id.closeTimeText);
            costText= itemView.findViewById(R.id.costText);
            imageRecyclerView = itemView.findViewById(R.id.image_recycler_view);
            directionBtn = itemView.findViewById(R.id.directionButton);
            menuBtn = itemView.findViewById(R.id.menuButton);
            callBtn = itemView.findViewById(R.id.callButton);

        }
    }
}
>>>>>>> f83cf2c30ead21302432d1bf36412a4be78d4e07
