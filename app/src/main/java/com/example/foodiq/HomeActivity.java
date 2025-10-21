package com.example.foodiq;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private TextView Address;
    private EditText search_bar;
    private LinearLayout veg_res, non_veg_res, cusion_res, home, scan, setting, profile;
    private RecyclerView restaurant_details;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private List<restaurant> restaurantList;
    private RestaurantAdapter restaurantAdapter;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        loadRestaurants();
    }

    private void initializeFirebase() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://foodiq-42857-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("restaurant");
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
            showToast("Failed to initialize database");
        }
    }

    private void initializeViews() {
        Address = findViewById(R.id.txtLocation);
        search_bar = findViewById(R.id.edtSearch);
        veg_res = findViewById(R.id.linear_larout_1);
        non_veg_res = findViewById(R.id.linear_layout_2);
        cusion_res = findViewById(R.id.linear_layout_3);
        home = findViewById(R.id.navHome);
        setting = findViewById(R.id.navSettings);
        scan = findViewById(R.id.navScan);
        profile = findViewById(R.id.navProfile);
        restaurant_details = findViewById(R.id.recyclerRestaurants);
//        progressBar = findViewById(R.id.progress_bar);

        restaurantList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(this, restaurantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        restaurant_details.setLayoutManager(layoutManager);
        restaurant_details.setAdapter(restaurantAdapter);
        restaurant_details.setNestedScrollingEnabled(false);
    }

    private void setupClickListeners() {
        home.setOnClickListener(v -> {
            // Already on home page
        });

        setting.setOnClickListener(v -> {
            showToast("Settings clicked");
        });

        scan.setOnClickListener(v -> {
            showToast("Scan clicked");
        });

        profile.setOnClickListener(v -> {
            showToast("Profile clicked");
        });

        veg_res.setOnClickListener(v -> {
            filterRestaurantsByType("Veg");
        });

        non_veg_res.setOnClickListener(v -> {
            filterRestaurantsByType("Non-Veg");
        });

        cusion_res.setOnClickListener(v -> {
            showToast("Cuisine filter clicked");
        });
    }

    private void loadRestaurants() {
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

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable timeoutRunnable = () -> {
            if (progressBar.getVisibility() == View.VISIBLE) {
                changeInProgress(false);
                showToast("Data loading timeout. Please check your connection.");
                Log.e(TAG, "Data loading operation timed out");
            }
        };

        handler.postDelayed(timeoutRunnable, 15000);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                handler.removeCallbacks(timeoutRunnable);
                changeInProgress(false);

                restaurantList.clear();

                if (!snapshot.exists()) {
                    Log.d(TAG, "No data found at restaurant node");
                    showToast("No restaurants found");
                    return;
                }

                Log.d(TAG, "Data snapshot exists with " + snapshot.getChildrenCount() + " children");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        restaurant Restaurant = dataSnapshot.getValue(restaurant.class);
                        if (Restaurant != null) {
                            Restaurant.setId(dataSnapshot.getKey());
                            restaurantList.add(Restaurant);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing restaurant data: " + e.getMessage());
                    }
                }

                Log.d(TAG, "Total restaurants loaded: " + restaurantList.size());
                restaurantAdapter.notifyDataSetChanged();

                if (restaurantList.isEmpty()) {
                    showToast("No restaurants available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                handler.removeCallbacks(timeoutRunnable);
                changeInProgress(false);
                Log.e(TAG, "DatabaseError: " + error.getMessage());
                showToast("Failed to load restaurants: " + error.getMessage());
            }
        });
    }

    private void filterRestaurantsByType(String type) {
        showToast("Filter feature coming soon");
    }

    private boolean isNetworkAvailable() {
        if (connectivityManager == null) return false;
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
        if (restaurant_details != null) {
            restaurant_details.setVisibility(inProgress ? View.GONE : View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}