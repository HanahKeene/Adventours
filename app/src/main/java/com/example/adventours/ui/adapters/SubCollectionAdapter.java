package com.example.adventours.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.example.adventours.R;

public class SubCollectionAdapter extends RecyclerView.Adapter<SubCollectionAdapter.ViewHolder> {

    private List<String> subCollectionNames;

    public SubCollectionAdapter(List<String> subCollectionNames) {
        this.subCollectionNames = subCollectionNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reservation_format_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subCollectionName = subCollectionNames.get(position);
        holder.bind(subCollectionName);
    }

    @Override
    public int getItemCount() {
        return subCollectionNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView subCollectionNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subCollectionNameTextView = itemView.findViewById(R.id.day);
        }

        public void bind(String subCollectionName) {
            subCollectionNameTextView.setText(subCollectionName);
        }
    }
}

