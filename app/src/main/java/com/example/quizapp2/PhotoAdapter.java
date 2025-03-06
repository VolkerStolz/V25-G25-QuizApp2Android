package com.example.quizapp2;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;
    private Context context;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(Photo photo);
    }
    public PhotoAdapter(Context context, List<Photo> photos, OnPhotoClickListener listener) {
        this.context = context;
        this.photos = photos;
        this.listener = listener;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.nameTextView.setText(photo.getName());
        if (photo.getImageResId() != null) {
            holder.imageView.setImageResource(photo.getImageResId());
        } else if (photo.getImageUri() != null && !photo.getImageUri().isEmpty()) {
            try {
                Uri imageUri = Uri.parse(photo.getImageUri());
                Log.d("PhotoAdapter", "Loading image URI: " + imageUri.toString());
                holder.imageView.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Optional: set a placeholder image if needed
            holder.imageView.setImageResource(R.drawable.gorilla);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener !=null) {
                listener.onPhotoClick(photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.textViewName);
        }
    }
}

