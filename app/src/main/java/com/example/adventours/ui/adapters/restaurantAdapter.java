package com.example.adventours.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.RestaurantsModel;

import java.util.List;

public class restaurantAdapter extends RecyclerView.Adapter<restaurantAdapter.ViewHolder> {

    private OnRestauItemClickListener onRestauItemClickListener;
    private Context context;
    private List<RestaurantsModel> restaurantsModelList;

    public restaurantAdapter(Context context, List<RestaurantsModel> restaurantsModelList, OnRestauItemClickListener onRestauItemClickListener) {
        this.context = context;
        this.restaurantsModelList = restaurantsModelList;
        this.onRestauItemClickListener = onRestauItemClickListener;
    }

    public interface OnRestauItemClickListener{
        void onRestauItemClick(String restauId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.restaucarousel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(restaurantsModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(restaurantsModelList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            if (onRestauItemClickListener != null) {
                onRestauItemClickListener.onRestauItemClick(restaurantsModelList.get(position).getRestau_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String restau_id = restaurantsModelList.get(position).getRestau_id();
                        // Check if the item ID is null
                        if (restau_id != null) {
                            // You can also call the onHotelItemClickListener method here to perform other actions
                            onRestauItemClickListener.onRestauItemClick(restau_id);
                        } else {
                            // If the item ID is null, log an error message
                            Log.e("restaurantAdapter", "Item ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("restaurantAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }
            });
        }
    }
}
