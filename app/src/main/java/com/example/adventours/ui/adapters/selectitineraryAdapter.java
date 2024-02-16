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

import java.util.List;

public class selectitineraryAdapter extends RecyclerView.Adapter<selectitineraryAdapter.ViewHolder> {

    private OnItineraryItemClickListener onItineraryClickListener;
    private Context context;
    private List<ItineraryModel> itineraryModelList;

    public interface OnItineraryItemClickListener {
        void onItineraryItemClick(String Id);
    }

    public selectitineraryAdapter(Context context, List<ItineraryModel> itineraryModelList, OnItineraryItemClickListener listener) {
        this.context = context;
        this.itineraryModelList = itineraryModelList;
        this.onItineraryClickListener = listener;
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
                            Intent intent = new Intent(context, itineraryplan.class);
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