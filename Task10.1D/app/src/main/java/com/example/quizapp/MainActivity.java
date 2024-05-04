package com.example.quizapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText loginUsernameEditText;
    private EditText loginPassword;
    private MyApplication myApp;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loginUsernameEditText = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        myApp = (MyApplication) getApplication();
        db = myApp.getDbHelper();
    }


    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {
        AlertDialog progressDialog = MuskUtil.showProgressDialog(this);
        db.loginWithUsername(this, loginUsernameEditText.getText().toString(), loginPassword.getText().toString());
        progressDialog.dismiss();
    }
}