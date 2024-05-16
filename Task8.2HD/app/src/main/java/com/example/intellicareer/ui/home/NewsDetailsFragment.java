package com.example.intellicareer.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.intellicareer.MyApplication;
import com.example.intellicareer.R;
import com.example.intellicareer.databinding.FragmentNewsDetailsBinding;


public class NewsDetailsFragment extends Fragment {
    private FragmentNewsDetailsBinding binding;
    private MyApplication myApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
        News selectedNews = myApplication.getSelectedNews();
        binding.newsDetailsImageView.setImageResource(selectedNews.getImageRes());
        binding.newsDetailsContentTextView.setText(selectedNews.getTitle());
        binding.newsDetailsContentTextView.setText(selectedNews.getContent());
    }
}