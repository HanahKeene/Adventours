package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;

import java.util.List;

public class individualItineraryactivityAdapter extends RecyclerView.Adapter<individualItineraryactivityAdapter.ViewHolder>{

    private Context context;
    private List<String> individualitineraryactivityModelList;

    public individualItineraryactivityAdapter(Context context, List<String> individualitineraryactivityModelList) {
        this.context = context;
        this.individualitineraryactivityModelList = individualitineraryactivityModelList;

    }

    public void setActivities(List<String> activities) {
        this.individualitineraryactivityModelList = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public individualItineraryactivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new individualItineraryactivityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_plan_activities, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull individualItineraryactivityAdapter.ViewHolder holder, int position) {
        holder.activity.setText(individualitineraryactivityModelList.get(position));
    }


    @Override
    public int getItemCount() {
        return individualitineraryactivityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        TextView activity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.activityname);
        }
    }
}