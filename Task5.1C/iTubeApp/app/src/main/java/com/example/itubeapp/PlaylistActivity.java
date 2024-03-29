package com.example.itubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp.database.DatabaseHelper;

import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private int UID;
    private List<Video> VideoList;
    private DatabaseHelper DBHelper;
    private RecyclerView PlayListRecyclerView;
    private TextView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        emptyView = findViewById(R.id.emptyView);
        UID = ((MyApp) getApplicationContext()).getUID();
        PlayListRecyclerView = findViewById(R.id.playListRecyclerView);
        DBHelper = DatabaseHelper.getInstance(this);
        VideoList = DBHelper.getUserPlaylist(UID);
        if (VideoList.isEmpty()) {
            PlayListRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            PlayListRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            setUpRecyclerView();
        }
    }
    private void setUpRecyclerView() {
        RecyclerView.LayoutManager playlistManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        PlayListRecyclerView.setLayoutManager(playlistManager);
        VideoRecyclerviewAdapter playlistAdapter = new VideoRecyclerviewAdapter(VideoList, this, position -> {
            Video clickedVideo = VideoList.get(position);
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("VIDEO_URL", clickedVideo.getURL());
            startActivity(intent);
        });
        PlayListRecyclerView.setAdapter(playlistAdapter);
    }
    public void backToHome(View view){
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void clearAll(View view){
        DBHelper.clearUserPlaylist(UID);
        Intent intent=new Intent(this,PlaylistActivity.class);
        startActivity(intent);
    }
}