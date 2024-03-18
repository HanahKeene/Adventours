package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;

import com.example.adventours.ui.models.MyItineraryCalendarModel;

import java.util.List;

public class MyItineraryCalendarAdapter extends RecyclerView.Adapter<MyItineraryCalendarAdapter.ReservationViewHolder> {

    private List<MyItineraryCalendarModel> myItineraryCalendarModelList;

    public MyItineraryCalendarAdapter(List<MyItineraryCalendarModel> myItineraryCalendarModelList) {
        this.myItineraryCalendarModelList = myItineraryCalendarModelList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_itineraryitem, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.date.setText(myItineraryCalendarModelList.get(position).getCheckIn() + " - " + myItineraryCalendarModelList.get(position).getCheckOut());
        holder.activityname.setText(myItineraryCalendarModelList.get(position).getHotelName());
    }

    @Override
    public int getItemCount() {
        return myItineraryCalendarModelList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {

        private TextView date, activityname;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            activityname = itemView.findViewById(R.id.activityname);
        }
    }
}
