package com.example.intellicareer.ui.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intellicareer.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public NewsAdapter(List<News> newsList, OnItemClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news, listener);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsPic;
        private TextView newsTitle;
        private TextView newsDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsPic = itemView.findViewById(R.id.news_image_view);
            newsTitle = itemView.findViewById(R.id.news_title_text_view);
            newsDate = itemView.findViewById(R.id.news_created_date_text_view);
        }

        public void bind(News news, OnItemClickListener listener) {
            newsPic.setImageResource(news.getImageRes());
            newsTitle.setText(news.getTitle());
            newsDate.setText(news.getDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(news);
                }
            });
        }
    }
}
