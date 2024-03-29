package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private GestureDetector gestureDetector;
    private int newsImageId;
    private String newsTitle;
    private String newsContent;
    private List<News> newsList;
    private ImageView selectedNewsImageView;
    private TextView selectedNewsTitleTextView;
    private TextView selectedNewsContentTextView;

    private int[] newsImageIds = {R.drawable.news_image_1, R.drawable.news_image_2, R.drawable.news_image_3, R.drawable.news_image_4};
    private String[] newsTitles = {"Fans left horrified as F1 driver plea ignored",
            "World braces as Princess set to disappear",
            "Man dead after jumping off popular bridge",
            "Thousands of babies eligible for new jab"};
    private String[] newsContents = {"Fans have been left in disbelief after a star’s pleas for help fell on deaf ears following a devastating Australian Grand Prix crash.",
            "A day after the Princess of Wales revealed she has cancer, Kensington Palace is poised to try to pull off something wholly unprecedented.",
            "The body of a tourist has been recovered after he tragically died while jumping from a popular tourist spot with a group of people."
            , "Thousands of babies who are most at risk from a worrying virus will be eligible for a new vaccination rollout."};

    private RecyclerView relatedStoryRecyclerView;
    public NewsFragment() {

    }


    public static NewsFragment newInstance(int newsImageId, String newsTitle, String newsContent) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt("NEWS_IMAGE_ID", newsImageId);
        args.putString("NEWS_TITLE", newsTitle);
        args.putString("NEWS_CONTENT", newsContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsImageId = getArguments().getInt("NEWS_IMAGE_ID");
            newsTitle = getArguments().getString("NEWS_TITLE");
            newsContent = getArguments().getString("NEWS_CONTENT");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        selectedNewsImageView = view.findViewById(R.id.selectedNewsImageView);
        selectedNewsTitleTextView = view.findViewById(R.id.selectedNewsTitleTextView);
        selectedNewsContentTextView = view.findViewById(R.id.selectedNewsContentTextView);
        relatedStoryRecyclerView=view.findViewById(R.id.relatedStoryRecyclerView);
        newsList = new ArrayList<>();
        for(int i = 0; i < newsImageIds.length; i++) {
            newsList.add(new News(newsImageIds[i], newsTitles[i], newsContents[i]));
        }
        RecyclerView.LayoutManager relatedStoryManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        relatedStoryRecyclerView.setLayoutManager(relatedStoryManager);
        RelatedStoryRecyclerViewAdapter relatedStoryAdapter = new RelatedStoryRecyclerViewAdapter(newsList, getActivity(), position -> {
            News clickedNews = newsList.get(position);
            selectedNewsImageView.setImageResource(clickedNews.getId());
            selectedNewsTitleTextView.setText(clickedNews.getTitle());
            selectedNewsContentTextView.setText(clickedNews.getContent());
        });

        relatedStoryRecyclerView.setAdapter(relatedStoryAdapter);
        selectedNewsImageView.setImageResource(newsImageId);
        selectedNewsTitleTextView.setText(newsTitle);
        selectedNewsContentTextView.setText(newsContent);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 0) {
                    FragmentManager fragmentManager = NewsFragment.this.getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.remove(NewsFragment.this);
                    transaction.commit();
                    showMainContentViews();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        LinearLayout selectedNewsLinearLayout = view.findViewById(R.id.selectedNewsLinearLayout);
        selectedNewsLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }
    private void showMainContentViews() {
        getActivity().findViewById(R.id.mainContent).setVisibility(View.VISIBLE);
    }
}