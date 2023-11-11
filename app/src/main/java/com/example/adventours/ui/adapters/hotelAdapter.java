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
import com.example.adventours.ui.models.FYPModel;
import com.example.adventours.ui.models.HotelsModel;
import com.example.adventours.ui.models.MusttryModel;

import java.util.List;

public class hotelAdapter extends RecyclerView.Adapter<hotelAdapter.ViewHolder> {

    private Context context;
    private List<HotelsModel> hotelsModelList;

    public hotelAdapter(Context context, List<HotelsModel> hotelsModelList) {
        this.context = context;
        this.hotelsModelList = hotelsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotelcarousel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(hotelsModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(hotelsModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return hotelsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.place);
        }
    }
}
