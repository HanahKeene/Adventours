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
import com.example.adventours.ui.models.activereservationModel;
import com.example.adventours.ui.models.activityModel;

import java.util.List;

public class activereservationAdapter extends RecyclerView.Adapter<activereservationAdapter.ViewHolder> {

    private Context context;
    private List<activereservationModel> activereservationModelList;

    public activereservationAdapter(Context context, List<activereservationModel> activereservationModelList) {
        this.context = context;
        this.activereservationModelList = activereservationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_check_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.roomname.setText( activereservationModelList.get(position).getRoomName());
        holder.hotelname.setText( activereservationModelList.get(position).getHotelName());
        holder.reservationid.setText( activereservationModelList.get(position).getReservationId());
        holder.date.setText( activereservationModelList.get(position).getCheckIn() + " - "+ activereservationModelList.get(position).getCheckOut());
        holder.status.setText( activereservationModelList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return  activereservationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView roomname, hotelname, reservationid, date, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomname = itemView.findViewById(R.id.reservation_item);
            hotelname = itemView.findViewById(R.id.reservation_place);
            reservationid = itemView.findViewById(R.id.reservation_num);
            date = itemView.findViewById(R.id.reservation_duration);
            status = itemView.findViewById(R.id.reservation_status);

        }
    }
}
