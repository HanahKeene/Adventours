package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adventours.R;
import com.example.adventours.ui.models.searchModel;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.SearchViewHolder> {

    private List<searchModel> dataList;

    public void setData(List<searchModel> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        searchModel data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.place);
        }

        void bind(searchModel data) {
            textViewName.setText(data.getName());
        }
    }
}

