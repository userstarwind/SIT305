package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends AppCompatActivity {
    private Task currentTask;
    private MyApplication myApp;
    private TextView taskTitle;
    private TextView questionContent;
    Button[] questionOptionButtons;
    private Button submitButton;
    private TextView progressTextView;
    private ProgressBar quesProgressBar;
    private Integer selectedAnswer;
    private Boolean isAnswerSubmitted;
    private List<Boolean> answerArray;
    private int numOfQues;
    Integer completedNumOfQues;
    private int currentTaskIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        myApp = (MyApplication) getApplication();
        Intent intent = getIntent();
        currentTaskIndex = intent.getIntExtra("TASK_INDEX", 0);
        currentTask = myApp.getCurrentUser().getTaskList().get(currentTaskIndex);
        numOfQues = currentTask.getQuestionList().size();
        taskTitle = findViewById(R.id.quiz_topic_textview);
        questionOptionButtons = new Button[4];
        questionOptionButtons[0] = findViewById(R.id.question_option_a_button);
        questionOptionButtons[1] = findViewById(R.id.question_option_b_button);
        questionOptionButtons[2] = findViewById(R.id.question_option_c_button);
        questionOptionButtons[3] = findViewById(R.id.question_option_d_button);
        submitButton = findViewById(R.id.submit_answer_button);
        questionContent=findViewById(R.id.question_content_textview);
        progressTextView=findViewById(R.id.progress_textview);
        quesProgressBar=findViewById(R.id.ques_progress_bar);
        isAnswerSubmitted = false;
        completedNumOfQues = 0;
        answerArray=new ArrayList<>();
        taskTitle.setText(currentTask.getTopic());
        submitButton=findViewById(R.id.submit_answer_button);
        for (int i = 0; i < questionOptionButtons.length; i++) {
            final int index = i;
            questionOptionButtons[i].setOnClickListener(view -> {
                resetButtonsBackground();
                selectedAnswer = index;
                view.setBackgroundResource(R.drawable.btn_submit_answer);
            });
        }
        submitButton.setOnClickListener(view -> {
            if (selectedAnswer == null) {
                Toast.makeText(this, "Please select your answer first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isAnswerSubmitted) {
                try {
                    showQuestionAnswer();
                    whetherAllowAnswer(false);
                    submitButton.setText("Next");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                selectedAnswer = null;
                completedNumOfQues++;
                if (completedNumOfQues == numOfQues) {
                    Intent Intent = new Intent(this, ResultActivity.class);
                    Intent.putExtra("RESULT_LIST",(Serializable)answerArray);
                    Intent.putExtra("TASK_INDEX",currentTaskIndex);
                    startActivity(Intent);
                    finish();
                    return;
                }
                resetButtonsBackground();
                updateProgress();
                whetherAllowAnswer(true);
                updateQuestion();
                submitButton.setText("Submit");
            }
            isAnswerSubmitted = !isAnswerSubmitted;
        });
        updateProgress();
        updateQuestion();
    }

    private void updateProgress() {
        progressTextView.setText(String.format("%d/%d", completedNumOfQues + 1, numOfQues));
        quesProgressBar.setProgress((int) ((((double) completedNumOfQues + 1) / (double) numOfQues * 100)));
    }
    private void updateQuestion() {
        Question currentQuestion=currentTask.getQuestionList().get(completedNumOfQues);
        questionContent.setText(currentQuestion.getQuestion());
        for (int i = 0; i < questionOptionButtons.length; i++) {
            questionOptionButtons[i].setText(currentQuestion.getOptions().get(i));
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
        Question currentQuestion=currentTask.getQuestionList().get(completedNumOfQues);
        int answer = currentQuestion.getFormattedAnswer();
        if (selectedAnswer == answer) {
            questionOptionButtons[selectedAnswer].setBackgroundResource(R.drawable.btn_true_answer);
            answerArray.add(true);
        } else {
            questionOptionButtons[selectedAnswer].setBackgroundResource(R.drawable.btn_false_answer);
            questionOptionButtons[answer].setBackgroundResource(R.drawable.btn_true_answer);
            answerArray.add(false);
        }
    }
}