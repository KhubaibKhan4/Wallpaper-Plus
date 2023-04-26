package com.example.wallpaperplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.wallpaperplus.Listeners.OnSelectedListener;
import com.example.wallpaperplus.Models.Photo;

import com.example.wallpaperplus.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    Context context;
    List<Photo> list;
    OnSelectedListener listener;

    public CustomAdapter(Context context, List<Photo> list, OnSelectedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.wallpaper_sample, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position).getSrc().medium)
                .into(holder.wallpaper_img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView wallpaper_img;
        CardView cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            wallpaper_img = itemView.findViewById(R.id.wallpaper_image);
            cardView = itemView.findViewById(R.id.main_containers);
        }
    }
}
