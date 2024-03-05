package com.example.adventours.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.SettingsActivity;
import com.example.adventours.restauinfo;
import com.example.adventours.touristspotinfo;
import com.example.adventours.ui.home.HomeFragment;
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.HotelListModel;
import com.example.adventours.ui.models.activityModel;
import com.example.adventours.ui.models.searchModel;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {

    private OnSearchItemClickListener onSearchItemClickListener;
    private Context context;
    private List<searchModel> searchModelList;

    public interface OnSearchItemClickListener {
        void onSearchItemClick(String itemId);
    }

    public searchAdapter(Context context, List<searchModel> searchModelList, OnSearchItemClickListener listener) {
        this.context = context;
        this.searchModelList = searchModelList;
        this.onSearchItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(searchModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return searchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.place);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // Get the clicked item position
            if (position != RecyclerView.NO_POSITION) {
                String itemId = searchModelList.get(position).getId(); // Get the ID of the clicked item
                onSearchItemClickListener.onSearchItemClick(itemId); // Notify the click listener
            }
        }
    }
}