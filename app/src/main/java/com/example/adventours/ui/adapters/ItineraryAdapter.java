package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.ItineraryModel;

import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {

    private List<ItineraryModel> itineraryList;

    public ItineraryAdapter(List<ItineraryModel> itineraryList) {
        this.itineraryList = itineraryList;
    }

    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_cover_format, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {

        ItineraryModel itinerary = itineraryList.get(position);
        holder.nameTextView.setText(itinerary.getName());
        Glide.with(holder.itemView.getContext()).load(itinerary.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        // Declare views in your item layout here
        TextView nameTextView;
        ImageView imageView;

        public ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            nameTextView = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.cover);
        }
    }
}

