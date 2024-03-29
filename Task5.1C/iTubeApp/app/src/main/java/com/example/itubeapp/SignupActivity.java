package com.example.itubeapp;

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

import com.example.itubeapp.database.DatabaseHelper;

public class SignupActivity extends AppCompatActivity {
    private EditText FullNameEditText;
    private EditText UserNameEditText;
    private EditText PasswordEditText;
    private EditText ConfirmPasswordEditText;
    private DatabaseHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        FullNameEditText = findViewById(R.id.signFullNameEditText);
        UserNameEditText = findViewById(R.id.signUserNameEditText);
        PasswordEditText = findViewById(R.id.signPasswordEditText);
        ConfirmPasswordEditText = findViewById(R.id.signConfirmPasswordEditText);
        DBHelper = DatabaseHelper.getInstance(this);
    }

    public void createAccount(View view) {
        String username = UserNameEditText.getText().toString();
        String password = PasswordEditText.getText().toString();
        String confirmPassword = ConfirmPasswordEditText.getText().toString();
        String fullName = FullNameEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The password entered twice must be the same", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DBHelper.checkUserExists(username)) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHelper.addUser(username, fullName, password);
        Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}