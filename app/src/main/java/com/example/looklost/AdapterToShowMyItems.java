
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

public class AdapterToShowMyItems extends FirestoreRecyclerAdapter<ItemModel, AdapterToShowMyItems.ItemViewHolder> {

    private Context context;

    public <FirestoreRecyclerOptions> AdapterToShowMyItems(@NonNull FirestoreRecyclerOptions options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull ItemModel model) {
        holder.itemName.setText(model.getItemName());
        holder.itemCategory.setText(model.getItemCategory());

        if (model.getItemImage() != null) {
            holder.itemImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getItemImage()).into(holder.itemImage);
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click
            }
        });
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, parent, false);
        return new ItemViewHolder(view);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemCategory;
        public ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvTitle);
            itemCategory = itemView.findViewById(R.id.tvCategory);
            itemImage = itemView.findViewById(R.id.title_image);
        }
    }
}


