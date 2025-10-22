package com.example.foodiq;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiq.adapter.RestaurantAdapter;
import com.example.foodiq.model.restaurant;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private TextView txtLocation;
    private EditText edtSearch;
    private MaterialCardView cardVeg, cardNonVeg, cardCuisine;
    private LinearLayout navHome, navScan, navSettings, navProfile;
    private RecyclerView recyclerRestaurants;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private List<restaurant> restaurantList;
    private List<restaurant> filteredRestaurantList;
    private RestaurantAdapter restaurantAdapter;
    private ConnectivityManager connectivityManager;

    private String currentFilter = "All"; // "All", "Veg", "Non-Veg"
    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase first
        initializeFirebase();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchFunctionality();

        // Initialize connectivity manager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Load restaurants
        loadRestaurants();
    }

    private void initializeFirebase() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://foodiq-42857-default-rtdb.firebaseio.com/");
            // CORRECTED: Changed from "restaurants" to "restaurant" to match your database structure
            databaseReference = firebaseDatabase.getReference("restaurant");
            Log.d(TAG, "Firebase initialized successfully with restaurant node");
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
            showToast("Failed to initialize database");
        }
    }

    private void initializeViews() {
        txtLocation = findViewById(R.id.txtLocation);
        edtSearch = findViewById(R.id.edtSearch);
        cardVeg = findViewById(R.id.cardVeg);
        cardNonVeg = findViewById(R.id.cardNonVeg);
        cardCuisine = findViewById(R.id.cardCuisine);
        navHome = findViewById(R.id.navHome);
        navSettings = findViewById(R.id.navSettings);
        navScan = findViewById(R.id.navScan);
        navProfile = findViewById(R.id.navProfile);
        recyclerRestaurants = findViewById(R.id.recyclerRestaurants);
        progressBar = findViewById(R.id.progressBar);

        restaurantList = new ArrayList<>();
        filteredRestaurantList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(this, filteredRestaurantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerRestaurants.setLayoutManager(layoutManager);
        recyclerRestaurants.setAdapter(restaurantAdapter);
        recyclerRestaurants.setNestedScrollingEnabled(false);
    }

    private void setupClickListeners() {
        navHome.setOnClickListener(v -> {
            // Already on home page
            showToast("Home");
        });

        navSettings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, settingActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, profileActivity.class);
            startActivity(intent);
        });

        cardVeg.setOnClickListener(v -> {
            currentFilter = "Veg";
//            filterRestaurants();
            showToast("Showing Veg Restaurants");
        });

        cardNonVeg.setOnClickListener(v -> {
            currentFilter = "Non-Veg";
//            filterRestaurants();
            showToast("Showing Non-Veg Restaurants");
        });

        cardCuisine.setOnClickListener(v -> {
            showCuisineFilterDialog();
        });
    }

    private void setupSearchFunctionality() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSearching = s.length() > 0;
//                filterRestaurants();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadRestaurants() {
        // Check network connectivity before attempting to load data
        if (!isNetworkAvailable()) {
            showNetworkErrorDialog();
            return;
        }

        if (databaseReference == null) {
            Log.e(TAG, "Database reference is null");
            showToast("Database connection failed");
            return;
        }

        changeInProgress(true);

        // Set a timeout for the data loading operation
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable timeoutRunnable = () -> {
            if (progressBar.getVisibility() == View.VISIBLE) {
                changeInProgress(false);
                showToast("Data loading timeout. Please check your connection.");
                Log.e(TAG, "Data loading operation timed out");
            }
        };

        // Schedule timeout check after 15 seconds
        handler.postDelayed(timeoutRunnable, 15000);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Remove the timeout check
                handler.removeCallbacks(timeoutRunnable);
                changeInProgress(false);

                restaurantList.clear();

                if (!snapshot.exists()) {
                    Log.d(TAG, "No data found at restaurant node");
                    showToast("No restaurants found");
                    updateRestaurantList();
                    return;
                }

                Log.d(TAG, "Data snapshot exists with " + snapshot.getChildrenCount() + " children");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        restaurant restaurant = dataSnapshot.getValue(restaurant.class);
                        if (restaurant != null) {
                            restaurant.setId(dataSnapshot.getKey());
                            restaurantList.add(restaurant);
                            Log.d(TAG, "Loaded restaurant: " + restaurant.getRestaurantName());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing restaurant data: " + e.getMessage());
                    }
                }

                Log.d(TAG, "Total restaurants loaded: " + restaurantList.size());
                updateRestaurantList();

                if (restaurantList.isEmpty()) {
                    showToast("No restaurants available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Remove the timeout check
                handler.removeCallbacks(timeoutRunnable);
                changeInProgress(false);

                Log.e(TAG, "DatabaseError: " + error.getMessage());
                showToast("Failed to load restaurants: " + error.getMessage());
            }
        });
    }

    private void updateRestaurantList() {
        filteredRestaurantList.clear();
        filteredRestaurantList.addAll(restaurantList);
        restaurantAdapter.notifyDataSetChanged();
    }

//    private void filterRestaurants() {
//        String searchQuery = edtSearch.getText().toString().toLowerCase().trim();
//
//        filteredRestaurantList.clear();
//
//        for (restaurant restaurant : restaurantList) {
//            boolean matchesSearch = searchQuery.isEmpty() ||
//                    restaurant.getRestaurantName().toLowerCase().contains(searchQuery) ||
//                    (restaurant.getCuisineType() != null && restaurant.getCuisineType().toLowerCase().contains(searchQuery));
//
//            boolean matchesFilter = currentFilter.equals("All") ||
//                    (currentFilter.equals("Veg") && restaurant.isVeg()) ||
//                    (currentFilter.equals("Non-Veg") && !restaurant.isVeg());
//
//            if (matchesSearch && matchesFilter) {
//                filteredRestaurantList.add(restaurant);
//            }
//        }
//
//        restaurantAdapter.notifyDataSetChanged();
//
//        // Show message if no results found
//        if (filteredRestaurantList.isEmpty() && (!searchQuery.isEmpty() || !currentFilter.equals("All"))) {
//            showToast("No restaurants found matching your criteria");
//        }
//    }

    private void showCuisineFilterDialog() {
        String[] cuisines = {"All", "Italian", "Chinese", "Indian", "Mexican", "Japanese", "Thai", "American", "Mediterranean"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Cuisine")
                .setItems(cuisines, (dialog, which) -> {
                    String selectedCuisine = cuisines[which];
                    if (selectedCuisine.equals("All")) {
                        currentFilter = "All";
//                        filterRestaurants();
                        showToast("Showing All Restaurants");
                    } else {
                        filterByCuisine(selectedCuisine);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void filterByCuisine(String cuisine) {
        currentFilter = "Cuisine";

        filteredRestaurantList.clear();
        for (restaurant restaurant : restaurantList) {
            if (restaurant.getCuisineType() != null && restaurant.getCuisineType().equalsIgnoreCase(cuisine)) {
                filteredRestaurantList.add(restaurant);
            }
        }
        restaurantAdapter.notifyDataSetChanged();

        if (filteredRestaurantList.isEmpty()) {
            showToast("No " + cuisine + " restaurants found");
        } else {
            showToast("Showing " + cuisine + " Restaurants");
        }
    }

    private boolean isNetworkAvailable() {
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNetworkErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("Retry", (dialog, which) -> loadRestaurants())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void changeInProgress(boolean inProgress) {
        if (progressBar != null) {
            progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        }
        if (recyclerRestaurants != null) {
            recyclerRestaurants.setVisibility(inProgress ? View.GONE : View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any resources if needed
    }
}