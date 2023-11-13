package com.example.adventours.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.RoomDetails;
import com.example.adventours.ui.models.roomModel;

import java.util.List;

public class hotelDetailsAdapter extends RecyclerView.Adapter<hotelDetailsAdapter.ViewHolder> {

    private Context context;
    private List<roomModel> roomModelList;

    public hotelDetailsAdapter(Context context, List<roomModel> roomModelList) {
        this.context = context;
        this.roomModelList = roomModelList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.available_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(roomModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(roomModelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, roomname;
        Button viewdetailsbtn; // Add your button here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.room_image);
            name = itemView.findViewById(R.id.price);
            roomname = itemView.findViewById(R.id.room_name);
            viewdetailsbtn = itemView.findViewById(R.id.viewdetails); // Replace with your actual button ID

            viewdetailsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String roomId = roomModelList.get(position).getRoom_id(); // Replace with the actual method to get room ID
                        // Check if the room ID is null
                        if (roomId != null) {
                            Toast.makeText(context, "Room ID: " + roomId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, RoomDetails.class);
                            intent.putExtra("ROOM_ID", roomId);
                            context.startActivity(intent);
                        } else {
                            // If the room ID is null, log an error message
                            Log.e("roomAdapter", "Room ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("roomAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }
            });
        }
    }
}
