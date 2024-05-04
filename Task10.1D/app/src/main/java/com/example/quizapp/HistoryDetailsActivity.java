package com.example.quizapp;

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

public class HistoryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Intent intent=getIntent();
        int currentCompletedTaskIndex = intent.getIntExtra("TASK_INDEX", 0);
        Toolbar toolbar = findViewById(R.id.history_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        RecyclerView recyclerView=findViewById(R.id.history_details_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyApplication myApp=(MyApplication) getApplication();
        CompletedTask currentCompletedTask=myApp.getCurrentUser().getCompletedTaskList().get(currentCompletedTaskIndex);
        recyclerView.setAdapter(new ResultAdapter(currentCompletedTask.getTask().getQuestionList(), currentCompletedTask.getAnswerList()));
    }
}