package com.example.adventours.ui.adapters.itineraryadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adventours.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.ui.models.itinerarymodels.ActivityModel;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<String> days;
    private List<List<ActivityModel>> activities;

    public DayAdapter(List<String> days, List<List<ActivityModel>> activities) {
        this.days = days;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reservation_format_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String day = days.get(position);
        List<ActivityModel> activityList = activities.get(position);

        holder.dayTextView.setText(day);

        ActivityAdapter activityAdapter = new ActivityAdapter(activityList);
        holder.activityRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.activityRecyclerView.setAdapter(activityAdapter);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        RecyclerView activityRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day);
            activityRecyclerView = itemView.findViewById(R.id.activityRecyclerView);
        }
    }
}

