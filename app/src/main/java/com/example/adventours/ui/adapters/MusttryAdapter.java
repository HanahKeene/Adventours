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
import com.example.adventours.ui.models.MusttryModel;

import java.util.List;

public class MusttryAdapter extends RecyclerView.Adapter<MusttryAdapter.ViewHolder> {

    private Context context;
    private List<MusttryModel> musttryModelList;

    public MusttryAdapter(Context context, List<MusttryModel> musttryModelList) {
        this.context = context;
        this.musttryModelList = musttryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.musttry_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(musttryModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(musttryModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return musttryModelList.size();
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
