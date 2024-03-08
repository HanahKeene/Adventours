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
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.tourModel;

import java.util.List;

public class tourAdapter extends RecyclerView.Adapter<tourAdapter.ViewHolder> {
    private Context context;
    private List<tourModel> tourModelList;

    public tourAdapter(Context context, List<tourModel> tourModelList) {
        this.context = context;
        this.tourModelList = tourModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tourinclusion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(tourModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return tourModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
