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
import com.example.adventours.ui.models.HotelListModel;

import java.util.List;

public class hotelListAdapter extends RecyclerView.Adapter<hotelListAdapter.ViewHolder> {

    private OnHotelListItemClickListener onHotelListItemClickListener;

    private Context context;
    private List<HotelListModel> hotelListModelList;

    public interface OnHotelListItemClickListener
    {
        void onHotelListItemClick(String hotel_id);
    }

    public hotelListAdapter(Context context, List<HotelListModel> hotelListModelList, OnHotelListItemClickListener listener) {
        this.context = context;
        this.hotelListModelList = hotelListModelList;
        this.onHotelListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_lists, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(hotelListModelList.get(position).getImg_url()).into(holder.catImg);
        holder.catName.setText(hotelListModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return hotelListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView catName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.pic);
            catName = itemView.findViewById(R.id.place);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String hotel_id = hotelListModelList.get(position).getHotel_id();
                        // Check if the item ID is null
                        if (hotel_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onHotelListItemClickListener.onHotelListItemClick(hotel_id);
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
