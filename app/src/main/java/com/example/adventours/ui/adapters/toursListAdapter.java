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
import com.example.adventours.ui.models.ToursListModel;

import java.util.List;

public class toursListAdapter extends RecyclerView.Adapter<toursListAdapter.ViewHolder> {

    private OnToursListItemClickListener onToursListItemClickListener;

    private Context context;
    private List<ToursListModel> toursListModelList;

    public interface OnToursListItemClickListener
    {
        void onTourListItemClick(String tour_id);
    }

    public toursListAdapter(OnToursListItemClickListener onToursListItemClickListener, Context context, List<ToursListModel> toursListModelList) {
        this.onToursListItemClickListener = onToursListItemClickListener;
        this.context = context;
        this.toursListModelList = toursListModelList;
    }

    @NonNull
    @Override
    public toursListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new toursListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_lists, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(toursListModelList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(toursListModelList.get(position).getName());
        holder.price.setText("₱" + toursListModelList.get(position).getPrice() + " per pax");
    }

    @Override
    public int getItemCount() {
        return toursListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.place);
            price = itemView.findViewById(R.id.price);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String tour_id = toursListModelList.get(position).getTour_id();
                        // Check if the item ID is null
                        if (tour_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onToursListItemClickListener.onTourListItemClick((tour_id));
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
