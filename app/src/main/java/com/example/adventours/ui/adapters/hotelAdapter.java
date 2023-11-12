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

import java.util.List;

public class hotelAdapter extends RecyclerView.Adapter<hotelAdapter.ViewHolder> {

    private OnHotelItemClickListener onHotelItemClickListener; // Renamed to match the interface
    private Context context;
    private List<HotelsModel> hotelsModelList;

    public hotelAdapter(Context context, List<HotelsModel> hotelList, OnHotelItemClickListener onHotelItemClickListener) {
        this.context = context;
        this.hotelsModelList = hotelList;
        this.onHotelItemClickListener = onHotelItemClickListener;
    }

    public interface OnHotelItemClickListener {
        void onHotelItemClick(String hotelId); // Renamed to match the interface
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotelcarousel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(hotelsModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(hotelsModelList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            if (onHotelItemClickListener != null) {
                onHotelItemClickListener.onHotelItemClick(hotelsModelList.get(position).getHotel_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String hotel_id = hotelsModelList.get(position).getHotel_id();
                        // Check if the item ID is null
                        if (hotel_id != null) {
                            // You can also call the onHotelItemClickListener method here to perform other actions
                            onHotelItemClickListener.onHotelItemClick(hotel_id);
                        } else {
                            // If the item ID is null, log an error message
                            Log.e("hotelAdapter", "Item ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("hotelAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }
            });
        }
    }
}
