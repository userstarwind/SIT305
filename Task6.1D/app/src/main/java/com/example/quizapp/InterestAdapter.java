package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestViewHolder> {
    private List<Interest> interestList;
    private OnInterestClickListener onInterestClickListener;

    public interface OnInterestClickListener {
        void onInterestClick(int position);
    }

    public InterestAdapter(List<Interest> interestList, OnInterestClickListener listener) {
        this.interestList = interestList;
        this.onInterestClickListener = listener;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item, parent, false);
        return new InterestViewHolder(itemView, onInterestClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        Interest interest = interestList.get(position);
        holder.interestButton.setText(interest.getName());
        holder.interestButton.setSelected(interest.isSelected());
    }

    @Override
    public int getItemCount() {
        return interestList != null ? interestList.size() : 0;
    }

    public class InterestViewHolder extends RecyclerView.ViewHolder {
        private Button interestButton;

        public InterestViewHolder(@NonNull View itemView, final OnInterestClickListener listener) {
            super(itemView);
            interestButton = itemView.findViewById(R.id.button_interest);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onInterestClick(position);
                }
            });
        }
    }
}

