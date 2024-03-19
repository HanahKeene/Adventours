package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.EventsListModel;
import com.example.adventours.ui.models.activereservationModel;
import com.example.adventours.ui.models.activityModel;

import java.util.List;

public class activereservationAdapter extends RecyclerView.Adapter<activereservationAdapter.ViewHolder> {

    private OnActiveReservationListItemClickListener onActiveReservationListItemClickListener;
    private Context context;
    private List<activereservationModel> activereservationModelList;

    public interface OnActiveReservationListItemClickListener
    {
        void onActiveReservationListItemClick(String reservation_id, String reservationType);
    }

    public activereservationAdapter(Context context, List<activereservationModel> activereservationModelList, OnActiveReservationListItemClickListener onActiveReservationListItemClickListener) {
        this.context = context;
        this.activereservationModelList = activereservationModelList;
        this.onActiveReservationListItemClickListener = onActiveReservationListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_check_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        activereservationModel reservation = activereservationModelList.get(position);

        if (reservation.getHotelName() != null) {
            holder.roomname.setText(reservation.getRoomName());
            holder.restauname.setText(reservation.getHotelName());
            holder.date.setText(reservation.getCheckIn() + " - " + reservation.getCheckOut());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActiveReservationListItemClickListener.onActiveReservationListItemClick(reservation.getReservationId(), "Hotel Reservation");
                }
            });
        } else if (reservation.getRestaurantName() != null) {
            holder.roomname.setText(reservation.getGuests());
            holder.restauname.setText(reservation.getRestaurantName());
            holder.date.setText(reservation.getDate() + " " + reservation.getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onActiveReservationListItemClickListener.onActiveReservationListItemClick(reservation.getReservationId(), "Restaurant Reservation");
                }
            });
        }

        holder.reservationid.setText("Reservation No. " + reservation.getReservationId());
        holder.status.setText(reservation.getStatus());
        holder.status.setTextColor(getStatusColor(reservation.getStatus()));
    }



    @Override
    public int getItemCount() {
        return  activereservationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView roomname, restauname, reservationid, date, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomname = itemView.findViewById(R.id.reservation_item);
            restauname = itemView.findViewById(R.id.reservation_place);
            reservationid = itemView.findViewById(R.id.reservation_num);
            date = itemView.findViewById(R.id.reservation_duration);
            status = itemView.findViewById(R.id.reservation_status);

            // Set click listener for the item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the clicked item
            int position = getAdapterPosition();
            // Check if listener is not null and the position is valid
            if (position != RecyclerView.NO_POSITION && onActiveReservationListItemClickListener != null) {
                // Retrieve the corresponding reservation ID and reservation type
                activereservationModel reservation = activereservationModelList.get(position);
                String reservationId = reservation.getReservationId();
                String reservationType = (reservation.getHotelName() != null) ? "Hotel Reservation" : "Restaurant Reservation";
                // Call the listener method
                onActiveReservationListItemClickListener.onActiveReservationListItemClick(reservationId, reservationType);
            }
        }
    }

    private int getStatusColor(String status) {
        int color;
        switch (status.toLowerCase()) {
            case "confirmed":
                color = ContextCompat.getColor(context, R.color.CONFIRMED);
                break;
            case "checkin":
                color = ContextCompat.getColor(context, R.color.CHECKIN);
                break;
            case "in progress":
                color = ContextCompat.getColor(context, R.color.INPROGRESS);
                break;
            case "on hold":
                color = ContextCompat.getColor(context, R.color.ONHOLD);
                break;
            case "pending approval":
                color = ContextCompat.getColor(context, R.color.PENDINGAPPROVAL);
                break;
            case "upcoming":
                color = ContextCompat.getColor(context, R.color.UPCOMING);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.black); // Set a default color if status doesn't match
        }
        return color;
    }
}
