package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView totalQuesTextView;
    private TextView correctlyTextView;
    private TextView inCorrectlyTextView;
    private TextView fullnameTextView;
    private ImageView avatarImageView;
    private Integer totalQues;
    private Integer correctAnswer;
    private Integer inCorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MyApplication myApp = (MyApplication) getApplication();
        Toolbar toolbar = findViewById(R.id.activity_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        totalQuesTextView = findViewById(R.id.profile_total_text_view);
        correctlyTextView = findViewById(R.id.profile_correct_text_view);
        inCorrectlyTextView = findViewById(R.id.profile_incorrect_text_view);
        fullnameTextView = findViewById(R.id.profile_fullname_text_view);
        avatarImageView = findViewById(R.id.profile_image_view);
        inCorrectAnswer = 0;
        correctAnswer = 0;
        totalQues = 0;
        fullnameTextView.setText(myApp.getCurrentUser().getFullName());
        Glide.with(this)
                .load(myApp.getCurrentUser().getAvatarUrl())
                .circleCrop()
                .into(avatarImageView);
        List<CompletedTask> completedTaskList = myApp.getCurrentUser().getCompletedTaskList();
        for (CompletedTask completedTask : completedTaskList) {
            totalQues += completedTask.getAnswerList().size();
            for (Boolean answer : completedTask.getAnswerList()) {
                if (answer) {
                    correctAnswer++;
                } else {
                    inCorrectAnswer++;
                }
            }
        }
        totalQuesTextView.setText(totalQues.toString());
        correctlyTextView.setText(correctAnswer.toString());
        inCorrectlyTextView.setText(inCorrectAnswer.toString());
    }

    public void shareProfile(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        if (totalQues > 0) {
            int score = (int) ((double) correctAnswer / totalQues * 100);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I scored " + score + "% correct on the quiz app.");
        } else {
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I'm using quiz app!");
        }

        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

}