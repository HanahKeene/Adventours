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
import com.example.adventours.ui.models.activityModel;
import com.example.adventours.ui.models.searchModel;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {

    private Context context;
    private List<searchModel> searchModelList;

    public searchAdapter(Context context, List<searchModel> searchModelList) {
        this.context = context;
        this.searchModelList = searchModelList;
        this.searchModelList = new ArrayList<>();
    }

    public void setData(List<searchModel> dataList) {
        this.searchModelList.clear();
        this.searchModelList.addAll(dataList);
        notifyDataSetChanged();
    }
    public void clearData() {
        this.searchModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(searchModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return  searchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.place);
        }
    }
}

