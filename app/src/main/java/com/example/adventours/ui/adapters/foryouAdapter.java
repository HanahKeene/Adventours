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
import com.example.adventours.ui.models.CategoryModel;
import com.example.adventours.ui.models.foryouModel;

import java.util.List;

public class foryouAdapter extends RecyclerView.Adapter<foryouAdapter.ViewHolder> {

    private Context context;
    private List<foryouModel> foryouModelList;

    public foryouAdapter(Context context, List<foryouModel> foryouModelList) {
        this.context = context;
        this.foryouModelList = foryouModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.foryou_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(foryouModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(foryouModelList.get(position).getName());
        holder.location.setText(foryouModelList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return foryouModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.place);
            location = itemView.findViewById(R.id.location);
        }
    }
}
