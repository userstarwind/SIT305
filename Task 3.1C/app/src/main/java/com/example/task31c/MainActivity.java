package com.example.task31c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText;
    Integer numOfQues = 5;

    public void jumpToQuiz(View view) {
        String username = usernameEditText.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("NUM_OF_QUES", numOfQues);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.usernameEditText);
        Intent intent = getIntent();
        if(intent != null) {
            usernameEditText.setText(intent.getStringExtra("USER_NAME"));
            numOfQues = intent.getIntExtra("NUM_OF_QUES", 5);
        }

    }


}