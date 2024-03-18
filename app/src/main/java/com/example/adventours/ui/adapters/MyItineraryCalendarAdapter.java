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
import com.example.adventours.ui.models.MyItineraryCalendarModel;
import com.example.adventours.ui.models.ToursListModel;

import java.util.List;

public class MyItineraryCalendarAdapter extends RecyclerView.Adapter<MyItineraryCalendarAdapter.ViewHolder> {

    private Context context;
    private List<MyItineraryCalendarModel> myItineraryCalendarModelList;

    public void setReservations(List<MyItineraryCalendarModel> reservationsList) {
    }

    public interface OnToursListItemClickListener
    {
        void onTourListItemClick(String tour_id);
    }

    public MyItineraryCalendarAdapter(Context context, List<MyItineraryCalendarModel> myItineraryCalendarModelList) {
        this.context = context;
        this.myItineraryCalendarModelList = myItineraryCalendarModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_itineraryitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(myItineraryCalendarModelList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return myItineraryCalendarModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, activity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            activity = itemView.findViewById(R.id.activityname);
        }
    }
}
