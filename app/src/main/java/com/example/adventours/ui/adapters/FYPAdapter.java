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

import java.util.List;

public class FYPAdapter extends RecyclerView.Adapter<FYPAdapter.ViewHolder> {

    private Context context;
    private List<FYPModel> fypModelList;

    public FYPAdapter(Context context, List<FYPModel> fypModelList) {
        this.context = context;
        this.fypModelList = fypModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.foryou_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(fypModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(fypModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return fypModelList.size();
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
