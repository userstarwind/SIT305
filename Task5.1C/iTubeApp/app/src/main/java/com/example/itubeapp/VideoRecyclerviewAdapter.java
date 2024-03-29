package com.example.itubeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoRecyclerviewAdapter extends RecyclerView.Adapter<VideoRecyclerviewAdapter.ViewHolder> {
    private List<Video> VideoList;
    private Context context;

    public VideoRecyclerviewAdapter(List<Video> videoList, Context context, OnItemClickListener listener) {
        VideoList = videoList;
        this.context = context;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    @NonNull
    @Override
    public VideoRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.url_item, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoRecyclerviewAdapter.ViewHolder holder, int position) {
        holder.VideoItemTextView.setText(VideoList.get(position).getURL());
    }

    @Override
    public int getItemCount() {
        return VideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView VideoItemTextView;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            VideoItemTextView=itemView.findViewById(R.id.videoItemTextView);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
