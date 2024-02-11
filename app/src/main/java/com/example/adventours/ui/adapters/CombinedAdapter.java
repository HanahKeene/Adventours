package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adventours.R;
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.RestaurantsModel;

import java.util.List;

public class CombinedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HOTEL = 0;
    private static final int VIEW_TYPE_RESTAURANT = 1;

    private List<Object> combinedResults;

    public CombinedAdapter(List<Object> combinedResults) {
        this.combinedResults = combinedResults;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_TYPE_HOTEL:
                View hotelView = inflater.inflate(R.layout.search_item, parent, false);
                viewHolder = new HotelViewHolder(hotelView);
                break;
            case VIEW_TYPE_RESTAURANT:
                View restaurantView = inflater.inflate(R.layout.search_item, parent, false);
                viewHolder = new RestaurantViewHolder(restaurantView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = combinedResults.get(position);

        if (item instanceof HotelsModel) {
            ((HotelViewHolder) holder).bind((HotelsModel) item);
        } else if (item instanceof RestaurantsModel) {
            ((RestaurantViewHolder) holder).bind((RestaurantsModel) item);
        }
    }

    @Override
    public int getItemCount() {
        return combinedResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = combinedResults.get(position);

        if (item instanceof HotelsModel) {
            return VIEW_TYPE_HOTEL;
        } else if (item instanceof RestaurantsModel) {
            return VIEW_TYPE_RESTAURANT;
        }

        return super.getItemViewType(position);
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(HotelsModel item) {
        }
        // ViewHolder for hotels
        // Implement constructor and bind method
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(RestaurantsModel item) {
        }
        // ViewHolder for restaurants
        // Implement constructor and bind method
    }
}
