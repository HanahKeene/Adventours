package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.ui.models.InterestModel;

import java.util.List;

public class interestAdapter extends RecyclerView.Adapter<interestAdapter.ViewHolder> {

    private Context context;
    private List<InterestModel> interestModelList;

    public interestAdapter(Context context, List<InterestModel> interestModelList) {
        this.context = context;
        this.interestModelList = interestModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.interestbtn, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.activity.setText(interestModelList.get(position).getActivity());
    }

    @Override
    public int getItemCount() {
        return interestModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button activity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.interestbtn);
        }
    }
}
