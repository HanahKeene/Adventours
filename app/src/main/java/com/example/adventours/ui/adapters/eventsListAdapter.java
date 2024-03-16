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
import com.example.adventours.ui.models.EventsListModel;
import com.example.adventours.ui.models.HotelListModel;

import java.util.List;

public class eventsListAdapter extends RecyclerView.Adapter<eventsListAdapter.ViewHolder> {

    private OnEventListItemClickListener onEventListItemClickListener;

    private Context context;
    private List<EventsListModel> eventsListModelList;

    public interface OnEventListItemClickListener
    {
        void onEventListItemClickListener(String event_id);
    }

    public eventsListAdapter(Context context, List<EventsListModel> eventsListModelList, OnEventListItemClickListener listener) {
        this.context = context;
        this.eventsListModelList = eventsListModelList;
        this.onEventListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.events_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(eventsListModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(eventsListModelList.get(position).getName());
        holder.date.setText(eventsListModelList.get(position).getStart_date() + " - " + eventsListModelList.get(position).getEnd_date());
    }

    @Override
    public int getItemCount() {
        return eventsListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, date, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.event_img);
            name = itemView.findViewById(R.id.eventname);
            date = itemView.findViewById(R.id.date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String events_id = eventsListModelList.get(position).getEvents_id();
                        // Check if the item ID is null
                        if (events_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onEventListItemClickListener.onEventListItemClickListener(events_id);
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
