package com.example.adventours.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventours.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class individualItineraryactivityAdapter extends RecyclerView.Adapter<individualItineraryactivityAdapter.ViewHolder>{

    private Context context;
    private List<String> individualitineraryactivityModelList;


    public individualItineraryactivityAdapter(Context context, List<String> individualitineraryactivityModelList) {
        this.context = context;
        this.individualitineraryactivityModelList = individualitineraryactivityModelList;

    }

    public void setActivities(List<String> activities) {
        this.individualitineraryactivityModelList = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public individualItineraryactivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new individualItineraryactivityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_plan_activities, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull individualItineraryactivityAdapter.ViewHolder holder, int position) {
        holder.activity.setText(individualitineraryactivityModelList.get(position));
    }


    @Override
    public int getItemCount() {
        return individualitineraryactivityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton remove;

        TextView activity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity = itemView.findViewById(R.id.activityname);
            remove = itemView.findViewById(R.id.removebtn);

            remove.setOnClickListener(v -> {
                // Get the document ID associated with this item
                DocumentSnapshot snapshot = activitySnapshotList.get(getAdapterPosition());
                String documentId = snapshot.getId();

                // Display the document ID in a toast message
                Toast.makeText(context, "Document ID: " + documentId, Toast.LENGTH_SHORT).show();

                // Optionally, you can also perform other actions here, such as removing the item from the list
            });
        }

//        private void showConfirmationDialog(final int position) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setMessage("Are you sure you want to remove this activity?")
//                    .setPositiveButton("Yes", (dialog, which) -> removeActivity(position))
//                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
//                    .show();
//        }

//        private void removeActivity(DocumentSnapshot activitySnapshot) {
//
//            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//            // Get the document ID of the activity to remove
//            String activityId = activitySnapshot.getId();
//
//            // Delete the activity from Firestore
//            activitiesCollection.document(activityId)
//                    .delete()
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(context, "Activity removed successfully", Toast.LENGTH_SHORT).show();
//                        // Optional: Remove the item from the RecyclerView list locally
//                        int position = activitySnapshotList.indexOf(activitySnapshot);
//                        if (position != -1) {
//                            activitySnapshotList.remove(position);
//                            notifyItemRemoved(position);
//                        }
//                    })
//                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove activity: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//        }
    }
}