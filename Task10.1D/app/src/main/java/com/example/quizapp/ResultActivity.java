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
    private MyApplication myApp;
    private DBHelper db;
    int currentTaskIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        myApp=(MyApplication) getApplication();
        db=myApp.getDbHelper();
        Intent intent = getIntent();
        answerList = (ArrayList<Boolean>) intent.getSerializableExtra("RESULT_LIST");
        currentTaskIndex = intent.getIntExtra("TASK_INDEX", 0);
        questionList = myApp.getCurrentUser().getTaskList().get(currentTaskIndex).getQuestionList();
        resultRecyclerView = findViewById(R.id.result_recyclerview);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setAdapter(new ResultAdapter(questionList, answerList));
    }

    public void backToHome(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        Task task=myApp.getCurrentUser().getTaskList().remove(currentTaskIndex);
        myApp.getCurrentUser().getCompletedTaskList().add(new CompletedTask(task,answerList));
        db.updateUserCompletedTaskListById(this,myApp.getCurrentUser().getId(),myApp.getCurrentUser().getCompletedTaskList());
        db.updateUserTaskListById(this,myApp.getCurrentUser().getId(),myApp.getCurrentUser().getTaskList());
        startActivity(intent);
        finish();
    }
}