package com.example.looklost;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

//        MAY NOT BE USED
public class FaceImageSearchadapter extends RecyclerView.Adapter<FaceImageSearchadapter.ImageViewHolder> {

    private List<String> imageUrls;
    private List<FaceImageSearch> searchResults;
    public void setSearchResults(List<FaceImageSearch> searchResults) {
        this.searchResults = searchResults;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_result, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        FaceImageSearch searchResult = searchResults.get(position);
        String imageUrl = searchResult.getImageUrl();
        String title = searchResult.getTitle();
        String snippet = searchResult.getDescription();

        // Load and display the image using a library like Glide or Picasso
        // For example:
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imageView);

        holder.titleTextView.setText(title);
        holder.snippetTextView.setText(snippet);
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView snippetTextView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            snippetTextView = itemView.findViewById(R.id.snippet_text_view);
        }
    }
}