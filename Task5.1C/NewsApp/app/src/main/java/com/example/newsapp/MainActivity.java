package com.example.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<News> newsList;
    private int[] newsImageIds = {R.drawable.news_image_1, R.drawable.news_image_2, R.drawable.news_image_3, R.drawable.news_image_4};
    private String[] newsTitles = {"Fans left horrified as F1 driver plea ignored",
            "World braces as Princess set to disappear",
            "Man dead after jumping off popular bridge",
            "Thousands of babies eligible for new jab"};
    private String[] newsContents = {"Fans have been left in disbelief after a star’s pleas for help fell on deaf ears following a devastating Australian Grand Prix crash.",
            "A day after the Princess of Wales revealed she has cancer, Kensington Palace is poised to try to pull off something wholly unprecedented.",
            "The body of a tourist has been recovered after he tragically died while jumping from a popular tourist spot with a group of people."
            , "Thousands of babies who are most at risk from a worrying virus will be eligible for a new vaccination rollout."};

    private RecyclerView newsRecyclerView;

    private RecyclerView topStoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        newsList = new ArrayList<>();
        for(int i = 0; i < newsImageIds.length; i++) {
            newsList.add(new News(newsImageIds[i], newsTitles[i], newsContents[i]));
        }

        newsRecyclerView=findViewById(R.id.newsRecyclerView);
        RecyclerView.LayoutManager newsLayoutManager = new GridLayoutManager(this, 2);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        NewsRecyclerViewAdapter newsAdapter = new NewsRecyclerViewAdapter(newsList, this, position -> {
            News clickedNews = newsList.get(position);
            hideMainContentViews();
            showNewsFragment(clickedNews);
        });
        newsRecyclerView.setAdapter(newsAdapter);

        newsRecyclerView.setAdapter(newsAdapter);

        topStoryRecyclerView=findViewById(R.id.topStoryRecyclerView);
        RecyclerView.LayoutManager topStoryManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topStoryRecyclerView.setLayoutManager(topStoryManager);
        TopStoryRecyclerViewAdapter topStoryAdapter=new TopStoryRecyclerViewAdapter(newsList,this,position -> {
            News clickedNews = newsList.get(position);
            hideMainContentViews();
            showNewsFragment(clickedNews);
        });
        topStoryRecyclerView.setAdapter(topStoryAdapter);



    }
    private void hideMainContentViews() {
        findViewById(R.id.mainContent).setVisibility(View.GONE);
    }

    private void showNewsFragment(News news) {
        NewsFragment newsFragment = NewsFragment.newInstance(news.getId(), news.getTitle(), news.getContent());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, newsFragment)
                .addToBackStack(null)
                .commit();
    }

}