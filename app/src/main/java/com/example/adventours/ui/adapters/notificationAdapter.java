package com.example.adventours.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.EventsListModel;
import com.example.adventours.ui.models.NotificationModel;
import com.example.adventours.ui.models.activereservationModel;
import com.example.adventours.ui.models.activityModel;


import java.util.List;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.ViewHolder> {

    private OnNotificationListItemClickListener onNotificationListItemClickListener;
    private Context context;
    private List<NotificationModel> notificationModelList;

    public interface OnNotificationListItemClickListener
    {
        void onNotificationItemClick(String document_id, String reservation_id, String reservation_category, String status);
    }

    public notificationAdapter(Context context, List<NotificationModel> notificationModelList , OnNotificationListItemClickListener onNotificationListItemClickListener) {
        this.context = context;
        this.notificationModelList = notificationModelList;
        this.onNotificationListItemClickListener = onNotificationListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel notification = notificationModelList.get(position);

        holder.title.setText(notification.getTitle());
        holder.details.setText(notification.getDescription());


        // Log the status
        Log.d("NotificationAdapter", "Status: " + notification.getStatus());

        // Apply text style based on the status
        if ("unread".equalsIgnoreCase(notification.getStatus())) {
            // Set title and description to bold if status is unread
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.details.setTypeface(null, Typeface.BOLD);
        } else {
            // Set title and description to normal typeface if status is not unread
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.details.setTypeface(null, Typeface.NORMAL);
        }
    }



    @Override
    public int getItemCount() {
        return  notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            details = itemView.findViewById(R.id.reservation_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        NotificationModel notification = notificationModelList.get(position);
                        onNotificationListItemClickListener.onNotificationItemClick(notification.getDocument_id(), notification.getReservation_id(), notification.getReservation_category(), notification.getStatus());
                    }
                }
            });
        }
    }
}
