package com.example.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    private List<Task> taskList;
    private Context context;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;
    @NonNull
    @Override
    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_row,parent,false);

        return new ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.taskTitleTextView.setText(taskList.get(position).getTitle());
        holder.dueDateTextView.setText("Due bty: "+taskList.get(position).getDueDate().toString());

    }

    public TaskRecyclerViewAdapter(List<Task> taskList, Context context, OnItemClickListener listener) {
        this.taskList = taskList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView taskTitleTextView;
        TextView dueDateTextView;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            taskTitleTextView=itemView.findViewById(R.id.taskTitleTextView);
            dueDateTextView=itemView.findViewById(R.id.dueDateTextView);
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
