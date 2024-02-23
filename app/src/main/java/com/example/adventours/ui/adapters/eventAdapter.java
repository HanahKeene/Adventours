package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.eventModel;
import com.example.adventours.ui.models.roomModel;

import java.util.List;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.ViewHolder> {

    private Context context;
    private List<eventModel> eventModelList;

    public eventAdapter(Context context, List<eventModel> eventModelList) {
        this.context = context;
        this.eventModelList = eventModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(eventModelList.get(position).getImg_url()).into(holder.img);
        holder.eventname.setText(eventModelList.get(position).getName());
        holder.desc.setText(eventModelList.get(position).getDesc());
    }


    @Override
    public int getItemCount() {
        return eventModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView desc, eventname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.event_image);
            desc = itemView.findViewById(R.id.desc);
            eventname = itemView.findViewById(R.id.activity_name);
        }
    }
}

