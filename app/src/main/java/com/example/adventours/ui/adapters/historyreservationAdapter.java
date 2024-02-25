package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.ui.models.activereservationModel;
import com.example.adventours.ui.models.historyreservationModel;

import java.util.List;

    public class historyreservationAdapter extends RecyclerView.Adapter<historyreservationAdapter.ViewHolder> {

        private Context context;
        private List<historyreservationModel> historyreservationModelList;

        public historyreservationAdapter(Context context, List<historyreservationModel> historyreservationModelList) {
            this.context = context;
            this.historyreservationModelList = historyreservationModelList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_check_item, parent, false);
            return new historyreservationAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.roomname.setText(historyreservationModelList.get(position).getRoomName());
            holder.hotelname.setText(historyreservationModelList.get(position).getHotelName());
            holder.reservationid.setText("Reservation No. " + historyreservationModelList.get(position).getReservationId());
            holder.date.setText(historyreservationModelList.get(position).getCheckIn() + " - " + historyreservationModelList.get(position).getCheckOut());
            holder.status.setText(historyreservationModelList.get(position).getStatus());

            holder.status.setTextColor(getStatusColor(historyreservationModelList.get(position).getStatus()));

        }

        @Override
        public int getItemCount() {
            return  historyreservationModelList.size();
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

        private int getStatusColor(String status) {
            int color;
            switch (status.toLowerCase()) {
                case "completed":
                    color = ContextCompat.getColor(context, R.color.COMPLETED);
                    break;
                case "cancelled":
                    color = ContextCompat.getColor(context, R.color.CANCELLED);
                    break;
                case "expired":
                    color = ContextCompat.getColor(context, R.color.EXPIRED);
                    break;
                case "no show":
                    color = ContextCompat.getColor(context, R.color.CANCELLED);
                    break;
                case "rejected":
                    color = ContextCompat.getColor(context, R.color.REJECTED);
                    break;
                default:
                    color = ContextCompat.getColor(context, R.color.black); // Set a default color if status doesn't match
            }
            return color;
        }
}
