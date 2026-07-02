package com.example.looklost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ShortListAdapter extends RecyclerView.Adapter<ShortListAdapter.MyViewHolder> {

    private Context context;
    private List<ItemModel> itemModelList;
    private OnItemClickListener onItemClickListener;

    public ShortListAdapter(Context context) {
        this.context = context;
        itemModelList = new ArrayList<>();
    }

    public void addItem(ItemModel itemModel){
        itemModelList.add(itemModel);
        notifyDataSetChanged();
    }

    public void clearItems(){
        itemModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_seemore, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel itemModel = itemModelList.get(position);
        if(itemModel.getItemImage()!=null){
            holder.title_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(itemModel.getItemImage()).into(holder.title_image);
        }
        else{
            holder.title_image.setVisibility(View.GONE);
        }
        holder.itemName.setText(itemModel.getItemName());
        holder.itemCategory.setText(itemModel.getItemCategory());
        holder.itemLocation.setText(itemModel.getItemLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(itemModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(itemModelList.size(), 3);
    }

    public interface OnItemClickListener {
        void onItemClick(ItemModel itemModel);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName, itemCategory,itemLocation;
        private ImageView title_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvTitle);
            itemCategory = itemView.findViewById(R.id.tvCategory);
            title_image = itemView.findViewById(R.id.title_image);
            itemLocation = itemView.findViewById(R.id.tvlocation);
        }
    }
}
