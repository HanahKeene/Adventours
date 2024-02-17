package com.example.adventours.ui.adapters.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.adventours.R;
import com.example.adventours.ui.models.Search.TouristSpotModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TouristSpotAdapter extends RecyclerView.Adapter<TouristSpotAdapter.TourViewHolder> {

    private List<TouristSpotModel> touristSpotModelList;

    public TouristSpotAdapter(List<TouristSpotModel> touristSpotModelList) {
        this.touristSpotModelList = touristSpotModelList;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        holder.touristspotname.setText(touristSpotModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return touristSpotModelList.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {

        private TextView touristspotname;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            touristspotname = itemView.findViewById(R.id.place);
        }
    }
}


