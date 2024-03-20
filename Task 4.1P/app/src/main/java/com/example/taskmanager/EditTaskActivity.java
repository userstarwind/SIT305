package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanager.database.DatabaseHelper;

import java.time.LocalDate;

public class EditTaskActivity extends AppCompatActivity {
    private Boolean whetherNewTask;
    private Task currentTask;
    private DatabaseHelper db;
    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;
    private TextView taskDueDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);
        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        taskDueDateTextView = findViewById(R.id.taskDueDateTextView);
        db = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        whetherNewTask = intent.getBooleanExtra("WHETHER_NEW_TASK", true);
        if (!whetherNewTask) {
            currentTask = new Task(intent.getIntExtra("TASK_ID", 0),
                    intent.getStringExtra("TASK_TITLE"),
                    intent.getStringExtra("TASK_DESCRIPTION"),
                    LocalDate.parse(intent.getStringExtra("TASK_DUE_DATE")));
            taskTitleEditText.setText(currentTask.getTitle());
            taskDescriptionEditText.setText(currentTask.getDescription());
            taskDueDateTextView.setText("Due Date: " + currentTask.getDueDate().toString());
        } else {
            currentTask = new Task(-1, "", "", null);
        }
    }

    public void backToHomePage(View view) {
        finish();
    }

    public void selectDueDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                (datePicker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth);
                    taskDueDateTextView.setText("Due Date: " + selectedDate.toString());
                    currentTask.setDueDate(selectedDate);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }


    public void saveTaskContent(View view) {
        if (!whetherNewTask) {
            currentTask.setTitle(taskTitleEditText.getText().toString());
            currentTask.setDescription(taskDescriptionEditText.getText().toString());
            db.updateTask(currentTask);
        } else {
            currentTask.setTitle(taskTitleEditText.getText().toString());
            currentTask.setDescription(taskDescriptionEditText.getText().toString());
            db.addTask(currentTask);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void deleteTask(View view) {
        db.deleteTask(currentTask);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}