package com.example.task31c;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {
    String username;
    Integer numOfQues;
    Integer score;
    TextView congratulationTextView;
    TextView scoreTextview;
    Button newQuizButton;
    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent intent = getIntent();
        username = intent.getStringExtra("USER_NAME");
        numOfQues = intent.getIntExtra("NUM_OF_QUES", 5);
        score = intent.getIntExtra("SCORE", 0);
        congratulationTextView = findViewById(R.id.congratulationTextView);
        scoreTextview = findViewById(R.id.scoreTextView);
        newQuizButton = findViewById(R.id.newQuizButton);
        finishButton = findViewById(R.id.finishButton);
        congratulationTextView.setText("Congratulation " + username + " !");
        scoreTextview.setText(String.format("%d/%d", score, numOfQues));
        newQuizButton.setOnClickListener(view -> {
            Intent nextIntent = new Intent(this, MainActivity.class);
            nextIntent.putExtra("USER_NAME", username);
            nextIntent.putExtra("NUM_OF_QUES", numOfQues);
            startActivity(nextIntent);
            finish();
        });
        finishButton.setOnClickListener(view -> {
            finish();
        });
    }
}