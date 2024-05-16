package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Task task);

        void onItemLongClick(Task task);
    }

    public TaskAdapter(List<Task> chatList, OnItemClickListener listener) {
        this.taskList = chatList;
        this.listener = listener;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task= taskList.get(position);
        holder.bind(task, listener);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;

        public TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title_textview);
            date = itemView.findViewById(R.id.task_created_date_textview);
        }

        public void bind(final Task task, final OnItemClickListener listener) {
            title.setText(task.getTopic());
            date.setText(task.getFormattedDate());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(task);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(task);
                    return true;
                }
            });
        }
    }
}
