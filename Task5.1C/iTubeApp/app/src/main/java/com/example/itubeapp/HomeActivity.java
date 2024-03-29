package com.example.itubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp.database.DatabaseHelper;

public class HomeActivity extends AppCompatActivity {
    private EditText YoutubeURLEditText;
    private DatabaseHelper DBHelper;

    private int UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        YoutubeURLEditText = findViewById(R.id.youtubeURLEditText);
        DBHelper = DatabaseHelper.getInstance(this);
        UID = ((MyApp) getApplicationContext()).getUID();
    }

    public void playVideo(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        if (!whetherURLisNull(YoutubeURLEditText)) {
            intent.putExtra("VIDEO_URL", YoutubeURLEditText.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please enter valid URL", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean whetherURLisNull(EditText url) {
        return url.getText().toString().isEmpty();
    }

    public void addToPlaylist(View view) {
        if (!whetherURLisNull(YoutubeURLEditText)) {
            DBHelper.addVideo(UID, YoutubeURLEditText.getText().toString());
            Toast.makeText(this, "Managed to add a video to you playlist", Toast.LENGTH_SHORT).show();
            YoutubeURLEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter valid URL", Toast.LENGTH_SHORT).show();
        }
    }

    public void myPlaylist(View view) {
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
        finish();
    }


}