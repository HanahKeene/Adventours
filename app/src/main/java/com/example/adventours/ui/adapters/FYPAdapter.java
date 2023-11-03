package com.example.adventours.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.FYPModel;

import java.util.List;

public class FYPAdapter extends RecyclerView.Adapter<FYPAdapter.ViewHolder> {

    private OnFYPItemClickListener onFYPItemClickListener;
    private Context context;
    private List<FYPModel> fypModelList;

    public interface OnFYPItemClickListener {
        void onFYPItemClick(String itemId);
    }

    public FYPAdapter(Context context, List<FYPModel> fypModelList, OnFYPItemClickListener listener) {
        this.context = context;
        this.fypModelList = fypModelList;
        this.onFYPItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.foryou_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(fypModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(fypModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return fypModelList.size();
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
                        String spot_id = fypModelList.get(position).getSpot_id();
                        // Check if the item ID is null
                        if (spot_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onFYPItemClickListener.onFYPItemClick(spot_id);
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