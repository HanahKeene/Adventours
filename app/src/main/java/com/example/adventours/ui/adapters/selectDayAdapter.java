package com.example.adventours.ui.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.adventours.R;
import com.example.adventours.ui.itineraryplan;
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.ItineraryModel;
import com.example.adventours.ui.models.selectDayModel;
import com.example.adventours.ui.selectDay;

import java.util.List;

public class selectDayAdapter extends RecyclerView.Adapter<selectDayAdapter.ViewHolder> {

    private OnDayItemClickListener onDayItemClickListener;
    private Context context;
    private List<selectDayModel> selectDayModelList;

    public interface  OnDayItemClickListener {
        void onDayItemClickListener(String Id);
    }

    public selectDayAdapter(Context context, List<selectDayModel> selectDayModelList, OnDayItemClickListener listener) {
        this.context = context;
        this.selectDayModelList = selectDayModelList;
        this.onDayItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_day_format, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(selectDayModelList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return selectDayModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.daycount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String id = selectDayModelList.get(position).getId();
                        if (id != null) {
                            Intent intent = new Intent(context, selectDay.class);
                            intent.putExtra("ItineraryID", id);
                            Toast.makeText(context, "Itinerary ID" + id, Toast.LENGTH_SHORT).show();
                            context.startActivity(intent);
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
