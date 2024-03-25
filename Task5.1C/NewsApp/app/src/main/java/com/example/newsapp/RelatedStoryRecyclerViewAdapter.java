package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RelatedStoryRecyclerViewAdapter extends RecyclerView.Adapter<RelatedStoryRecyclerViewAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public RelatedStoryRecyclerViewAdapter(List<News> newsList, Context context, OnItemClickListener listener) {
        this.newsList = newsList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RelatedStoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.related_story_item,parent,false);

        return new ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedStoryRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.relatedNewsTitleTextView.setText(newsList.get(position).getTitle());
        holder.relatedNewsContentTextView.setText(newsList.get(position).getContent());
        holder.relatedNewsImageView.setImageResource(newsList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView relatedNewsTitleTextView;
        TextView relatedNewsContentTextView;
        ImageView relatedNewsImageView;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            relatedNewsTitleTextView=itemView.findViewById(R.id.relatedNewsContentTextView);
            relatedNewsContentTextView=itemView.findViewById(R.id.relatedNewsContentTextView);
            relatedNewsImageView=itemView.findViewById(R.id.relatedNewsImageView);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
