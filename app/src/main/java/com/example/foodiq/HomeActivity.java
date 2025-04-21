package com.example.foodiq;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiq.adapter.RestaurantAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.foodiq.model.restaurant;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    TextView Address;
    EditText search_bar;
    LinearLayout veg_res , non_veg_res , cusion_res , home, scan,setting,profile;
    RecyclerView restaurant_details;
    private DatabaseReference databaseReference;
    private List<restaurant> restaurantList;
    private RestaurantAdapter restaurantAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("restaurant");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Address = findViewById(R.id.textView17);
        search_bar = findViewById(R.id.searchBar);
        veg_res = findViewById(R.id.linear_larout_1);
        non_veg_res = findViewById(R.id.linear_layout_2);
        cusion_res = findViewById(R.id.linear_layout_3);
        home = findViewById(R.id.linear_layout_4);
        setting = findViewById(R.id.linear_layout_5);
        scan = findViewById(R.id.linear_layout_6);
        profile = findViewById(R.id.linear_layout_7);
        restaurant_details = findViewById(R.id.recycler_view);




        restaurantList = new ArrayList<>();

        setUpRecyclerView();

        loadRestaurants();
    }



    private void setUpRecyclerView(){
        restaurantAdapter = new RestaurantAdapter(this,restaurantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        restaurant_details.setLayoutManager(layoutManager);
        restaurant_details.setAdapter(restaurantAdapter);
    }

    private void loadRestaurants(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurantList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    restaurant Restaurant = dataSnapshot.getValue(restaurant.class);
                    if(Restaurant!=null){
                        Restaurant.setId(dataSnapshot.getKey());
                        if(Restaurant.getImageUrl()!= null){
                            Log.d("Image debug ","Restaurant : " + Restaurant.getRestaurantName());
                            for(Map.Entry<String,String> entry : Restaurant.getImageUrl().entrySet()){
                                Log.d("Image debug" , entry.getKey() + ": " + entry.getValue());
                            }
                        }
                        restaurantList.add(Restaurant);
                    }
                }
                restaurantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("HomeActivity" , "DatabaseError: "+error.getMessage());

            }
        });
    }
}