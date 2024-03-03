package com.example.adventours.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.example.adventours.ui.models.individualitineraryactivityModel;

import java.util.List;

public class individualItineraryactivityAdapter extends RecyclerView.Adapter<individualItineraryactivityAdapter.ViewHolder>{

    private Context context;
    private List<individualitineraryactivityModel> individualitineraryactivityModelList;

    public individualItineraryactivityAdapter(Context context, List<individualitineraryactivityModel> individualitineraryactivityModelList) {
        this.context = context;
        this.individualitineraryactivityModelList = individualitineraryactivityModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_plan_activities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        individualitineraryactivityModel activityModel = individualitineraryactivityModelList.get(position);
        holder.activity.setText(activityModel.getName());
        holder.place.setText(activityModel.getPlace());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the document ID associated with this activity
                String documentId = activityModel.getDocumentId();
                // Display the document ID in a toast message
                Toast.makeText(context, "Document ID: " + documentId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return individualitineraryactivityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView remove;
        TextView activity, place;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.activity_name);
            place = itemView.findViewById(R.id.activity_place);
            remove = itemView.findViewById(R.id.removebtn);
        }
    }
}
