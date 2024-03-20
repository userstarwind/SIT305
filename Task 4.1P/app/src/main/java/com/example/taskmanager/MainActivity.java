package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.database.DatabaseHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    RecyclerView taskRecyclerView;
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    List<Task> taskList;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DatabaseHelper.getInstance(this);
        taskList = db.getAllTasks().stream()
                .sorted()
                .collect(Collectors.toList());
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        if (taskList.isEmpty()) {
            findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            findViewById(R.id.taskRecyclerView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.emptyView).setVisibility(View.GONE);
            findViewById(R.id.taskRecyclerView).setVisibility(View.VISIBLE);
        }
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(taskList, this, new TaskRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Task currentTask = taskList.get(position);
                Intent editTaskIntent = new Intent(MainActivity.this, EditTaskActivity.class);
                editTaskIntent.putExtra("WHETHER_NEW_TASK", false);
                editTaskIntent.putExtra("TASK_ID", currentTask.getId());
                editTaskIntent.putExtra("TASK_TITLE", currentTask.getTitle());
                editTaskIntent.putExtra("TASK_DESCRIPTION", currentTask.getDescription());
                editTaskIntent.putExtra("TASK_DUE_DATE", currentTask.getDueDate().toString());
                startActivity(editTaskIntent);
            }
        });
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskRecyclerViewAdapter);
    }

    public void addNewTask(View view) {
        Intent addTaskIntent = new Intent(this, EditTaskActivity.class);
        addTaskIntent.putExtra("WHETHER_NEW_TASK", true);
        startActivity(addTaskIntent);
    }


}