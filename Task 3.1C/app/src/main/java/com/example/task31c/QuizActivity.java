package com.example.task31c;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class QuizActivity extends AppCompatActivity {
    TextView usernameTextView;
    Integer numOfQues;
    Integer completedNumOfQues;
    String username;
    TextView progressTextView;
    ProgressBar quesProgressBar;
    JSONArray quesArray;
    TextView questionTitleTextView;
    TextView questionContentTextView;
    Button[] questionOptionButtons;
    Button sumbitButton;
    Integer selectedAnswer;
    Boolean isAnswerSubmitted;
    Integer score;

    private void updateProgress() {
        progressTextView.setText(String.format("%d/%d", completedNumOfQues + 1, numOfQues));
        quesProgressBar.setProgress((int) ((((double) completedNumOfQues + 1) / (double) numOfQues * 100)));
    }

    private void updateQuestion() throws JSONException {
        JSONObject ques = quesArray.getJSONObject(completedNumOfQues);
        questionTitleTextView.setText(ques.getString("title"));
        questionContentTextView.setText(ques.getString("content"));
        for (int i = 0; i < questionOptionButtons.length; i++) {
            String optionText = ques.getJSONArray("options").getString(i);
            questionOptionButtons[i].setText(optionText);
        }

    }

    private void resetButtonsBackground() {
        for (Button button : questionOptionButtons) {
            button.setBackgroundResource(R.drawable.btn_default_answer);
        }
    }

    private void whetherAllowAnswer(Boolean bool){
        for (Button button : questionOptionButtons) {
            button.setEnabled(bool);
        }
    }

    private void showQuestionAnswer() throws JSONException {
        JSONObject ques = quesArray.getJSONObject(completedNumOfQues);
        int answer = ques.getInt("answer");
        if (selectedAnswer == answer) {
            questionOptionButtons[selectedAnswer].setBackgroundResource(R.drawable.btn_true_answer);
            score++;
        } else {
            questionOptionButtons[selectedAnswer].setBackgroundResource(R.drawable.btn_false_answer);
            questionOptionButtons[answer].setBackgroundResource(R.drawable.btn_true_answer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        usernameTextView = findViewById(R.id.usernameTextView);
        Intent intent = getIntent();
        username = intent.getStringExtra("USER_NAME");
        numOfQues = intent.getIntExtra("NUM_OF_QUES", 5);
        completedNumOfQues = 0;
        usernameTextView.setText("Welcome " + username + " !");
        progressTextView = findViewById(R.id.progressTextView);
        quesProgressBar = findViewById(R.id.quesProgressBar);
        questionTitleTextView = findViewById(R.id.questionTitleTextView);
        questionContentTextView = findViewById(R.id.questionContentTextView);
        questionOptionButtons = new Button[3];
        questionOptionButtons[0] = findViewById(R.id.questionFirstOptionButton);
        questionOptionButtons[1] = findViewById(R.id.questionSecondOptionButton);
        questionOptionButtons[2] = findViewById(R.id.questionThirdOptionButton);
        sumbitButton = findViewById(R.id.submitButton);
        isAnswerSubmitted = false;
        score = 0;
        updateProgress();
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("ques.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            quesArray = new JSONArray(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            updateQuestion();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < questionOptionButtons.length; i++) {
            final int index = i;
            questionOptionButtons[i].setOnClickListener(view -> {
                resetButtonsBackground();
                selectedAnswer = index;
                view.setBackgroundResource(R.drawable.btn_submit_answer);
            });
        }
        sumbitButton.setOnClickListener(view -> {
            if (selectedAnswer == null) {
                Toast.makeText(this, "Please select your answer first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isAnswerSubmitted) {
                try {
                    showQuestionAnswer();
                    whetherAllowAnswer(false);
                    sumbitButton.setText("Next");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                selectedAnswer = null;
                completedNumOfQues++;
                if (completedNumOfQues == numOfQues) {
                    Intent nextIntent = new Intent(this, ScoreActivity.class);
                    nextIntent.putExtra("USER_NAME", username);
                    nextIntent.putExtra("NUM_OF_QUES", numOfQues);
                    nextIntent.putExtra("SCORE", score);
                    startActivity(nextIntent);
                    finish();
                    return;
                }
                resetButtonsBackground();
                updateProgress();
                whetherAllowAnswer(true);
                try {
                    updateQuestion();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                sumbitButton.setText("Submit");
            }
            isAnswerSubmitted = !isAnswerSubmitted;
        });


    }
}