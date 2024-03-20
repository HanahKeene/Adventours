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
import com.example.adventours.ui.models.ItineraryModel;
import com.example.adventours.ui.selectDay;

import java.util.List;

public class selectitineraryAdapter extends RecyclerView.Adapter<selectitineraryAdapter.ViewHolder> {

    private OnItineraryItemClickListener onItineraryClickListener;
    private Context context;
    private List<ItineraryModel> itineraryModelList;

    private String spot_id, restau_id, hotel_id, events_id, source;

    public interface OnItineraryItemClickListener {
        void onItineraryItemClick(String Id);
    }

    public selectitineraryAdapter(Context context, List<ItineraryModel> itineraryModelList, OnItineraryItemClickListener listener, String spot_id, String restau_id, String hotel_id, String events_id, String source) {
        this.context = context;
        this.itineraryModelList = itineraryModelList;
        this.spot_id = spot_id;
        this.restau_id = restau_id;
        this.hotel_id = hotel_id;
        this.events_id = events_id;
        this.source = source;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_cover_format, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(itineraryModelList.get(position).getImage()).apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL)).into(holder.img);
        holder.name.setText(itineraryModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return itineraryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.cover);
            name = itemView.findViewById(R.id.name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String id = itineraryModelList.get(position).getId();
                        if (id != null) {
                            Intent intent = new Intent(context, selectDay.class);
                            if (source.equals("Tourist Spot")) {
                                intent.putExtra("Source", "Tourist Spot");
                                intent.putExtra("Spot_ID", spot_id);
                                Toast.makeText(context, "Spot_ID" + spot_id + " Itinerary ID " + id, Toast.LENGTH_SHORT).show();
                            } else if (source.equals("Restaurant")) {
                                intent.putExtra("Spot_ID", restau_id);
                                Toast.makeText(context, "Restaurant ID" + restau_id + " Itinerary ID " + id, Toast.LENGTH_SHORT).show();
                            } else if (source.equals("Hotel")) {
                                intent.putExtra("Spot_ID", hotel_id);
                                Toast.makeText(context, "Hotel_ID" + hotel_id + " Itinerary ID " + id, Toast.LENGTH_SHORT).show();
                            } else if (source.equals("Events")) {
                                intent.putExtra("Source", "Events");
                                intent.putExtra("Spot_ID", events_id);
                                Toast.makeText(context, "Events ID" + events_id + " Itinerary ID " + id, Toast.LENGTH_SHORT).show();
                            }
                            intent.putExtra("ItineraryID", id);
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