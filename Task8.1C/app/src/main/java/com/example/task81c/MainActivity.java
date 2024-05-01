package com.example.task81c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.username_edit_text);
    }

    public void login(View view) {
        String username = usernameEditText.getText().toString();
        if (!username.isEmpty()) {
            Intent intent=new Intent(this,ChatActivity.class);
            intent.putExtra("USERNAME",username);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
        }
    }
}