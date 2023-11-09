package com.example.adventours.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adventours.R;
import com.example.adventours.ui.models.HotDealModel;

import java.util.List;

public class hotdealsAdapter extends RecyclerView.Adapter<hotdealsAdapter.ViewHolder> {

    private OnHotDealItemClickListener onHotDealItemClickListener;

    private Context context;
    private List<HotDealModel> hotDealModelList;

    public interface OnHotDealItemClickListener
    {
        void onHotDealsItemClick(String deal_id);
    }

    public hotdealsAdapter(Context context, List<HotDealModel> hotDealModelList, OnHotDealItemClickListener listener) {
        this.context = context;
        this.hotDealModelList = hotDealModelList;
        this.onHotDealItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hotdeal_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(hotDealModelList.get(position).getImg_url()).into(holder.catImg);
        holder.catName.setText(hotDealModelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return hotDealModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView catName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catImg = itemView.findViewById(R.id.cat_img);
            catName = itemView.findViewById(R.id.cat_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String deal_id = hotDealModelList.get(position).getHotdeal_id();
                        // Check if the item ID is null
                        if (deal_id != null) {
                            // You can also call the onFYPItemClickListener method here to perform other actions
                            onHotDealItemClickListener.onHotDealsItemClick(deal_id);
                        } else {
                            // If the item ID is null, log an error message
                            Log.e("FYPAdapter", "Item ID is null");
                        }
                    } else {
                        // If the item position is equal to RecyclerView.NO_POSITION, log an error message
                        Log.e("FYPAdapter", "Item position is equal to RecyclerView.NO_POSITION");
                    }
                }
            });
        }
    }
}
