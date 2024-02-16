package com.example.adventours.ui.adapters.itineraryadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adventours.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.models.itinerarymodels.ActivityModel;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<ActivityModel> activitiesModel;

    public ActivityAdapter(List<ActivityModel> activities) {
        this.activitiesModel = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_plan_activities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityModel activity = activitiesModel.get(position);
        holder.nameTextView.setText(activity.getName());
        // Load image using Glide or another image loading library
        // Example: Glide.with(holder.itemView.getContext()).load(activity.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return activitiesModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        // ImageView imageView; // Uncomment if you have an ImageView for the activity image

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.day);
            // imageView = itemView.findViewById(R.id.activityImage); // Uncomment if you have an ImageView for the activity image
        }
    }
}

