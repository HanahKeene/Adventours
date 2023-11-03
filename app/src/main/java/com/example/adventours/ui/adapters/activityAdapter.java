package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.activityModel;

import java.util.List;

public class activityAdapter extends RecyclerView.Adapter<activityAdapter.ViewHolder> {

    private Context context;
    private List<activityModel> activityModelList; // List of ActivityItem objects

    public activityAdapter(Context context, List<activityModel> activityModelList) {
        this.context = context;
        this.activityModelList = activityModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load( activityModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText( activityModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return  activityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.activity);
            name = itemView.findViewById(R.id.activity_name);
        }
    }
}

