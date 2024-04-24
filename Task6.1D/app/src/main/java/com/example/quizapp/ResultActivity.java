package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private List<Boolean> answerList;
    private List<Question> questionList;
    private RecyclerView resultRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        answerList = (ArrayList<Boolean>) intent.getSerializableExtra("RESULT_LIST");
        int currentTaskIndex = intent.getIntExtra("TASK_INDEX", 0);
        MyApplication myApp = (MyApplication) getApplication();
        questionList = myApp.getCurrentUser().getTaskList().get(currentTaskIndex).getQuestionList();
        resultRecyclerView = findViewById(R.id.result_recyclerview);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setAdapter(new ResultAdapter(questionList, answerList));
    }

    public void backToHome(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
        finish();
    }
}