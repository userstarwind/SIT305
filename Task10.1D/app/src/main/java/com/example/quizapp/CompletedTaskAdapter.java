package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletedTaskViewHolder> {
    private List<CompletedTask> completedTaskList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(CompletedTask completedTask);

        void onItemLongClick(CompletedTask completedTask);
    }

    public CompletedTaskAdapter(List<CompletedTask> completedTaskList, OnItemClickListener listener) {
        this.completedTaskList = completedTaskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompletedTaskAdapter.CompletedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new CompletedTaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTaskAdapter.CompletedTaskViewHolder holder, int position) {
        CompletedTask completedTask= completedTaskList.get(position);
        holder.bind(completedTask, listener);
    }

    @Override
    public int getItemCount() {
        return completedTaskList.size();
    }

    public class CompletedTaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        public CompletedTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title_textview);
            date = itemView.findViewById(R.id.task_created_date_textview);
        }

        public void bind(CompletedTask completedTask, OnItemClickListener listener) {
            title.setText(completedTask.getTask().getTopic());
            date.setText(completedTask.getTask().getFormattedDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(completedTask);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(completedTask);
                    return true;
                }
            });
        }
    }
}
