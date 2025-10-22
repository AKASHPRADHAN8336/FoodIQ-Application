package com.example.foodiq;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodiq.model.restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class restaurants_home_page extends AppCompatActivity {

    private TextView txtRestaurantName, txtDeliveryTime, txtDistance, txtLocation,
            txtOpenTime, txtCloseTime, txtPhone, txtWebsite;
    private ImageView imgBack;

    private DatabaseReference databaseReference;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurants_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        initializeFirebase();

        initializeViews();
        setupClickListeners();
        loadRestaurantData();
    }

    private void initializeFirebase() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://foodiq-42857-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("restaurant");
        } catch (Exception e) {
            Log.e("RestaurantHomePage", "Firebase initialization failed: " + e.getMessage());
            showToast("Failed to initialize database");
        }
    }

    private void initializeViews() {
        // Initialize all TextViews
        txtRestaurantName = findViewById(R.id.textView30);
        txtDeliveryTime = findViewById(R.id.textView31);
        txtDistance = findViewById(R.id.textView32);
        txtLocation = findViewById(R.id.textView29);
        txtOpenTime = findViewById(R.id.textView35);
        txtCloseTime = findViewById(R.id.textView37);
        txtPhone = findViewById(R.id.textView38);
        txtWebsite = findViewById(R.id.textView39);

        // Initialize ImageView for back button
        imgBack = findViewById(R.id.imageView10);
    }

    private void setupClickListeners() {
        // Back button click listener
        imgBack.setOnClickListener(v -> {
            finish(); // Close this activity and go back to HomeActivity
        });

        // Bottom navigation click listeners
        findViewById(R.id.imageView6).setOnClickListener(v -> showToast("Home"));
        findViewById(R.id.imageView7).setOnClickListener(v -> showToast("Settings"));
        findViewById(R.id.imageView12).setOnClickListener(v -> showToast("Scan"));
        findViewById(R.id.imageView13).setOnClickListener(v -> showToast("Profile"));

        // Tab click listeners
        findViewById(R.id.textView43).setOnClickListener(v -> showOverview());
        findViewById(R.id.textView42).setOnClickListener(v -> showMenu());
        findViewById(R.id.textView41).setOnClickListener(v -> showReviews());
        findViewById(R.id.textView40).setOnClickListener(v -> showPhotos());
    }

    private void loadRestaurantData() {
        // Get restaurant ID from intent
        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");

        if (restaurantId == null) {
            showToast("Restaurant not found");
            finish();
            return;
        }

        // First, set data from intent (quick display)
        setDataFromIntent();

        // Then load complete data from Firebase
        loadCompleteDataFromFirebase();
    }

    private void setDataFromIntent() {
        // Set data from intent for quick display
        String restaurantName = getIntent().getStringExtra("RESTAURANT_NAME");
        String price = getIntent().getStringExtra("RESTAURANT_PRICE");
        String openTime = getIntent().getStringExtra("RESTAURANT_OPEN_TIME");
        String closeTime = getIntent().getStringExtra("RESTAURANT_CLOSE_TIME");
        String deliveryTime = getIntent().getStringExtra("RESTAURANT_DELIVERY_TIME");
        String distance = getIntent().getStringExtra("RESTAURANT_DISTANCE");
        String address = getIntent().getStringExtra("RESTAURANT_ADDRESS");
        String phone = getIntent().getStringExtra("RESTAURANT_PHONE");
        String website = getIntent().getStringExtra("RESTAURANT_WEBSITE");

        // Set the data to views with null checks
        if (restaurantName != null) {
            txtRestaurantName.setText(restaurantName);
        }

        if (deliveryTime != null) {
            txtDeliveryTime.setText(deliveryTime);
        } else {
            txtDeliveryTime.setText("25 min"); // Default
        }

        if (distance != null) {
            txtDistance.setText(distance);
        } else {
            txtDistance.setText("1.2 km"); // Default
        }

        if (address != null) {
            txtLocation.setText(address);
        } else {
            txtLocation.setText("BTM Layout, Bangalore");
        }

        if (openTime != null) {
            txtOpenTime.setText(openTime);
        } else {
            txtOpenTime.setText("08:00");
        }

        if (closeTime != null) {
            txtCloseTime.setText(closeTime);
        } else {
            txtCloseTime.setText("22:00");
        }

        if (phone != null) {
            txtPhone.setText(phone);
        } else {
            txtPhone.setText("6264409094");
        }

        if (website != null) {
            txtWebsite.setText(website);
        } else {
            txtWebsite.setText("www.restaurant.com");
        }
    }

    private void loadCompleteDataFromFirebase() {
        if (databaseReference == null || restaurantId == null) {
            return;
        }

        databaseReference.child(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        restaurant restaurant = snapshot.getValue(restaurant.class);
                        if (restaurant != null) {
                            // Update UI with complete restaurant details from Firebase
                            updateUIWithFirebaseData(restaurant);
                        }
                    } catch (Exception e) {
                        Log.e("RestaurantHomePage", "Error parsing restaurant data: " + e.getMessage());
                    }
                } else {
                    Log.e("RestaurantHomePage", "No restaurant found with ID: " + restaurantId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RestaurantHomePage", "DatabaseError: " + error.getMessage());
                showToast("Failed to load restaurant details");
            }
        });
    }

    private void updateUIWithFirebaseData(restaurant restaurant) {
        // Update all fields with data from Firebase
        if (restaurant.getRestaurantName() != null) {
            txtRestaurantName.setText(restaurant.getRestaurantName());
        }

        if (restaurant.getDeliveryTime() != null) {
            txtDeliveryTime.setText(restaurant.getDeliveryTime());
        }

        if (restaurant.getDistance() != null) {
            txtDistance.setText(restaurant.getDistance());
        }

        if (restaurant.getAddress() != null) {
            txtLocation.setText(restaurant.getAddress());
        }

        if (restaurant.getOpeneingTime() != null) {
            txtOpenTime.setText(restaurant.getOpeneingTime());
        }

        if (restaurant.getClosingTime() != null) {
            txtCloseTime.setText(restaurant.getClosingTime());
        }

        if (restaurant.getPhone() != null) {
            txtPhone.setText(restaurant.getPhone());
        }

        if (restaurant.getWebsite() != null) {
            txtWebsite.setText(restaurant.getWebsite());
        }

        Log.d("RestaurantHomePage", "UI updated with Firebase data for: " + restaurant.getRestaurantName());
    }

    private void showOverview() {
        showToast("Overview clicked");
        // TODO: Implement overview section with restaurant details
    }

    private void showMenu() {
        showToast("Menu clicked");
        // TODO: Implement menu section with dishes list
    }

    private void showReviews() {
        showToast("Reviews clicked");
        // TODO: Implement reviews section
    }

    private void showPhotos() {
        showToast("Photos clicked");
        // TODO: Implement photos gallery
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}