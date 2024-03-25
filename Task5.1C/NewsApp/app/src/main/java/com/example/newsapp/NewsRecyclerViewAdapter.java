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

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public NewsRecyclerViewAdapter(List<News> newsList, Context context, OnItemClickListener listener) {
        this.newsList = newsList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);

        return new ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.newsTitleTextView.setText(newsList.get(position).getTitle());
        holder.newsContentTextView.setText(newsList.get(position).getContent());
        holder.newsImageView.setImageResource(newsList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitleTextView;
        TextView newsContentTextView;
        ImageView newsImageView;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            newsTitleTextView=itemView.findViewById(R.id.newsTitleTextView);
            newsContentTextView=itemView.findViewById(R.id.newsContentTextView);
            newsImageView=itemView.findViewById(R.id.newsImageView);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
