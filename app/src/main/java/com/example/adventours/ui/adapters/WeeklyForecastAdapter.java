package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import com.example.adventours.R;

public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastAdapter.WeeklyForecastViewHolder> {

    private List<WeeklyForecast> weeklyForecastList;

    public WeeklyForecastAdapter(List<WeeklyForecast> weeklyForecastList) {
        this.weeklyForecastList = weeklyForecastList;
    }

    @NonNull
    @Override
    public WeeklyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_forecast, parent, false);
        return new WeeklyForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyForecastViewHolder holder, int position) {
        WeeklyForecast weeklyForecast = weeklyForecastList.get(position);
        holder.dayTextView.setText(weeklyForecast.getDay());
        holder.minTempTextView.setText(weeklyForecast.getMinTemperature());
        holder.maxTempTextView.setText(weeklyForecast.getMaxTemperature());
        Picasso.get().load(weeklyForecast.getConditionIconUrl()).into(holder.iconImageView);
    }

    @Override
    public int getItemCount() {
        return weeklyForecastList.size();
    }

    public static class WeeklyForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, minTempTextView, maxTempTextView;
        ImageView iconImageView;

        public WeeklyForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day);
            minTempTextView = itemView.findViewById(R.id.temp);
//            maxTempTextView = itemView.findViewById(R.id.maxTempTextView);
//            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }
}
