package com.example.quizapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {
    private ImageView userAvatar;
    private TextView taskNumberTip;
    private RecyclerView taskRecyclerView;
    private TextView userFullName;
    private List<Task> taskList;
    private DBHelper db;
    private MyApplication myApp;
    private QuizGenerator quizGenerator;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        userAvatar = findViewById(R.id.login_imageView);
        taskNumberTip = findViewById(R.id.task_number_tip);
        userFullName = findViewById(R.id.login_full_name);
        taskRecyclerView = findViewById(R.id.task_recycler_view);
        myApp = (MyApplication) getApplication();
        db = myApp.getDbHelper();
        quizGenerator = new QuizGenerator();
        toolbar = findViewById(R.id.activity_task_toolbar);
        setSupportActionBar(toolbar);
        Glide.with(this)
                .load(myApp.getCurrentUser().getAvatarUrl())
                .circleCrop()
                .into(userAvatar);
        userFullName.setText(myApp.getCurrentUser().getFullName());
        taskNumberTip.setText("You have " + myApp.getCurrentUser().getTaskList().size() + " task due");
        taskList = myApp.getCurrentUser().getTaskList();
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(new TaskAdapter(taskList, new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(TaskActivity.this, QuizActivity.class);
                int taskIndex = taskList.indexOf(task);
                intent.putExtra("TASK_INDEX", taskIndex);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Task task) {
                showDeleteConfirmationDialog(task);
            }
        }));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_history) {
            Intent intent = new Intent(TaskActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_profile) {
            Intent intent = new Intent(TaskActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_upgrade) {
            Intent intent = new Intent(TaskActivity.this, UpgradeActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteConfirmationDialog(final Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask(Task task) {
        taskList.remove(task);
        db.updateUserTaskListById(this, myApp.getCurrentUser().getId(), taskList);
        updateLayout();
    }

    private void updateLayout() {
        taskNumberTip.setText("You have " + myApp.getCurrentUser().getTaskList().size() + " task due");
        taskRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void addTask(View view) {
        List<String> topics = myApp.getCurrentUser().getInterestList();
        if (!topics.isEmpty()) {
            Random random = new Random();
            String randomTopic = topics.get(random.nextInt(topics.size()));
            quizGenerator.fetchQuizQuestions(this, randomTopic, new Callback<Quiz>() {
                @Override
                public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                    if (response.isSuccessful()) {
                        Quiz quiz = response.body();
                        List<Question> questions = quiz.getQuiz();
                        taskList.add(new Task(randomTopic, new Date(), questions));
                        db.updateUserTaskListById(TaskActivity.this, myApp.getCurrentUser().getId(), taskList);
                        updateLayout();
                    } else {
                        Toast.makeText(TaskActivity.this, "Failed to get questions", Toast.LENGTH_SHORT).show();
                        Log.e("API Error", "Response Error: " + response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<Quiz> call, Throwable throwable) {
                    Toast.makeText(TaskActivity.this, "Failed to get questions", Toast.LENGTH_SHORT).show();
                    Log.e("API Failure", "Network or other error", throwable);
                }
            });
        } else {
            Intent intent = new Intent(TaskActivity.this, InterestActivity.class);
            startActivity(intent);
            finish();
        }
    }
}