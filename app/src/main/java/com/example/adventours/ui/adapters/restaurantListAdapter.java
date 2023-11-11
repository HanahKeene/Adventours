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
import com.example.adventours.ui.models.RestaurantListModel;

import java.util.List;

public class restaurantListAdapter extends RecyclerView.Adapter<restaurantListAdapter.ViewHolder> {

    private OnRestaurantItemClickListener onRestaurantItemClickListener;

    private Context context;
    private List<RestaurantListModel> restaurantListModelList;

    public interface OnRestaurantItemClickListener
    {
        void onRestaurantItemClick(String restau_id);
    }

    public restaurantListAdapter(Context context, List<RestaurantListModel> restaurantListModelList, OnRestaurantItemClickListener listener) {
        this.context = context;
        this.restaurantListModelList = restaurantListModelList;
        this.onRestaurantItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_lists, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(restaurantListModelList.get(position).getImg_url()).into(holder.catImg);
        holder.catName.setText(restaurantListModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return restaurantListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView catName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.pic);
            catName = itemView.findViewById(R.id.place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String restau_id = restaurantListModelList.get(position).getRestau_id();
                        // Check if the item ID is null
                        if (restau_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onRestaurantItemClickListener.onRestaurantItemClick(restau_id);
                        } else {
                            // If the item ID is null, log an error message
                            Log.e("FYPAdapter", "Item ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("FYPAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }
            });
        }
    }
}
