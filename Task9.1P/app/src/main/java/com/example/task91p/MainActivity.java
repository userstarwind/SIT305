package com.example.task91p;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void createNewAdvert(View view) {
        Intent intent=new Intent(this, PublishAdvertActivity.class);
        startActivity(intent);
    }

    public void showBoard(View view) {
        Intent intent=new Intent(this,BoardActivity.class);
        startActivity(intent);
    }

    public void showMap(View view) {
        Intent intent=new Intent(this,DistributionActivity.class);
        startActivity(intent);
    }
}