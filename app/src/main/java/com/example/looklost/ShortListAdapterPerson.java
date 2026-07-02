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

public class ShortListAdapterPerson extends RecyclerView.Adapter<ShortListAdapterPerson.MyViewHolder> {

    private Context context;
    private List<PersonModel> itemModelList;
    private OnItemClickListener onItemClickListener;

    public ShortListAdapterPerson(Context context) {
        this.context = context;
        itemModelList = new ArrayList<>();
    }

    public void addItem(PersonModel itemModel){
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
        PersonModel itemModel = itemModelList.get(position);
        if(itemModel.getPersonImage()!=null){
            holder.title_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(itemModel.getPersonImage()).into(holder.title_image);
        }
        else{
            holder.title_image.setVisibility(View.GONE);
        }
        holder.itemName.setText(itemModel.getPersonName());
        holder.itemCategory.setText(itemModel.getPersonCategory());
        holder.itemLocation.setText(itemModel.getPersonLocation());

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
        void onItemClick(PersonModel itemModel);
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
