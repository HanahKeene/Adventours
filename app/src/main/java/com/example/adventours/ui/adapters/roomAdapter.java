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
import com.example.adventours.ui.models.roomModel;

import java.util.List;

public class roomAdapter extends RecyclerView.Adapter<roomAdapter.ViewHolder> {

    private Context context;
    private List<roomModel> roomModelList;
    private OnItemClickListener itemClickListener;

    public roomAdapter(Context context, List<roomModel> roomModelList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.roomModelList = roomModelList;
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String roomId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.available_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(roomModelList.get(position).getImg_url()).into(holder.img);
        holder.roomname.setText(roomModelList.get(position).getName());
        holder.price.setText(roomModelList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView price, roomname;
        Button viewdetailsbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.room_image);
            price = itemView.findViewById(R.id.price);
            roomname = itemView.findViewById(R.id.room_name);
            viewdetailsbtn = itemView.findViewById(R.id.viewdetails);

            viewdetailsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String roomId = roomModelList.get(position).getRoom_id();
                        if (itemClickListener != null && roomId != null) {
                            itemClickListener.onItemClick(roomId);
                        }
                    }
                }
            });
        }
    }
}
