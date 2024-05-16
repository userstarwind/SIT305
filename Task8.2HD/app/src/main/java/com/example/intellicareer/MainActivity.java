package com.example.intellicareer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.intellicareer.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding binding;
private MyApplication myApplication;
private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myApplication=(MyApplication) getApplication();
        db=myApplication.getDbHelper();
        binding.loginSignUpTextView.setOnClickListener(this::navigateToRegisterActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        db.checkWhetherUserLogin(this);
    }

    private void navigateToRegisterActivity(View view) {
        Intent intent=new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void resetPassword(View view) {
        showResetPasswordDialog();
    }
    public void showResetPasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_reset_password, null);
        EditText emailEditText = view.findViewById(R.id.emailEditText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send", null)
                .create();

        dialog.show();

        dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email address can not be empty", Toast.LENGTH_SHORT).show();
            } else {
                db.resetPasswordByEmail(this,email);
                dialog.dismiss();
            }
        });
    }
    public void login(View view) {
        String email=binding.loginEmailEditText.getText().toString();
        String password=binding.loginPasswordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        db.loginWithEmailAndPassword(this,email,password);
    }


}