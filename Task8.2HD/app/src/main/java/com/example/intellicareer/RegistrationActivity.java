package com.example.intellicareer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.intellicareer.databinding.ActivityHomeBinding;
import com.example.intellicareer.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private MyApplication myApplication;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myApplication=(MyApplication) getApplication();
        db=myApplication.getDbHelper();
        binding.registerSignInTextView.setOnClickListener(this::navigateToRegisterActivity);
    }

    private void navigateToRegisterActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void register(View view) {
        String username=binding.registerUsername.getText().toString();
        String email=binding.registerEmail.getText().toString();
        String password=binding.registerPassword.getText().toString();
        String confirmPassword=binding.registerConfirmPassword.getText().toString();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "username, email and password can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The passwords entered twice are inconsistent", Toast.LENGTH_SHORT).show();
            return;
        }
        db.createNewUser(this,username,email,password);
    }
}