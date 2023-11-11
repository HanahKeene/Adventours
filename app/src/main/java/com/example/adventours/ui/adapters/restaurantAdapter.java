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
import com.example.adventours.ui.models.RestaurantsModel;

import java.util.List;

public class restaurantAdapter extends RecyclerView.Adapter<restaurantAdapter.ViewHolder> {

    private Context context;
    private List<RestaurantsModel> restaurantsModelList;

    public restaurantAdapter(Context context, List<RestaurantsModel> restaurantsModelList) {
        this.context = context;
        this.restaurantsModelList = restaurantsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.restaucarousel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(restaurantsModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(restaurantsModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return restaurantsModelList.size();
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
