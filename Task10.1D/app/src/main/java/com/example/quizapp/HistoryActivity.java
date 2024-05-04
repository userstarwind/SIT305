package com.example.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private List<CompletedTask> completedTaskList;
    private MyApplication myApp;
    private DBHelper db;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myApp = (MyApplication) getApplication();
        db = myApp.getDbHelper();
        completedTaskList = myApp.getCurrentUser().getCompletedTaskList();
        CompletedTaskAdapter completedTaskAdapter = new CompletedTaskAdapter(completedTaskList, new CompletedTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CompletedTask completedTask) {
                Intent intent = new Intent(HistoryActivity.this, HistoryDetailsActivity.class);
                int taskIndex = completedTaskList.indexOf(completedTask);
                intent.putExtra("TASK_INDEX", taskIndex);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(CompletedTask completedTask) {
                showDeleteConfirmationDialog(completedTask);
            }
        });
        recyclerView.setAdapter(completedTaskAdapter);

    }

    private void showDeleteConfirmationDialog(final CompletedTask completedTask) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCompletedTask(completedTask);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCompletedTask(CompletedTask completedTask) {
        completedTaskList.remove(completedTask);
        db.updateUserCompletedTaskListById(this, myApp.getCurrentUser().getId(), completedTaskList);
        updateLayout();
    }

    private void updateLayout() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}