package com.example.foodiq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiq.R;
import com.example.foodiq.model.restaurant;
import com.example.foodiq.restaurants_home_page;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private Context context;
    private List<restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurantsdetails,parent,false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {

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
