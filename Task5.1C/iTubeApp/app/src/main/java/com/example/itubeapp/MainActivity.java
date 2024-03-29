package com.example.itubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.itubeapp.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    private DatabaseHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        DBHelper=DatabaseHelper.getInstance(this);
    }

    public void login(View view) {
        Integer UID = DBHelper.checkUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        if (UID != null) {
            ((MyApp) getApplicationContext()).setUID(UID);
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void signup(View view){
        Intent intent=new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}