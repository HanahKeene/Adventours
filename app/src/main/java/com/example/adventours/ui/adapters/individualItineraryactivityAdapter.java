package com.example.adventours.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class individualItineraryactivityAdapter extends RecyclerView.Adapter<individualItineraryactivityAdapter.ViewHolder>{


    private Context context;
    private List<individualitineraryactivityModel> individualitineraryactivityModelList;

    public individualItineraryactivityAdapter(Context context, List<individualitineraryactivityModel> activities) {
        this.context = context;
        this.individualitineraryactivityModelList = activities;
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
                showConfirmationDialog(activityModel);
            }

            private void showConfirmationDialog(individualitineraryactivityModel activityModel) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Removal");
                builder.setMessage("Are you sure you want to remove this activity?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the activity from Firestore
                        removeActivityFromFirestore(activityModel);
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            }

            private void removeActivityFromFirestore(individualitineraryactivityModel activityModel) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user ID

                db.collection("users").document(userId)
                        .collection("itineraries").document(activityModel.getItineraryId())
                        .collection("days").document(activityModel.getDayId())
                        .collection("activities").document(activityModel.getDocumentId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Activity removed successfully", Toast.LENGTH_SHORT).show();
                                // Remove the activity from the list and notify the adapter
                                individualitineraryactivityModelList.remove(activityModel);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to remove activity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
