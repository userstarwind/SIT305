package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class InterestActivity extends AppCompatActivity {
    private RecyclerView interestRecyclerView;
private List<Interest> interestList;
private MyApplication myApp;
private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        interestRecyclerView=findViewById(R.id.interest_recyclerview);
        myApp=(MyApplication)getApplication();
        db=myApp.getDbHelper();
        interestList=new ArrayList<>();
        interestList.add(new Interest("Algorithm"));
        interestList.add(new Interest("Data Structure"));
        interestList.add(new Interest("Web Development"));
        interestList.add(new Interest("Testing"));
        interestList.add(new Interest("Machine Learning"));
        interestList.add(new Interest("Artificial Intelligence"));
        interestList.add(new Interest("Mobile Development"));
        interestList.add(new Interest("Systems Programming"));
        interestList.add(new Interest("Cyber Security"));
        interestList.add(new Interest("Cloud Computing"));
        interestList.add(new Interest("Databases"));
        interestList.add(new Interest("Game Development"));
        interestList.add(new Interest("Software Engineering Principles"));
        interestList.add(new Interest("User Interface Design"));
        interestList.add(new Interest("Operating Systems"));
        interestList.add(new Interest("Networks"));
        interestList.add(new Interest("Quality Assurance"));
        interestList.add(new Interest("Project Management"));
        interestList.add(new Interest("Big Data"));
        interestList.add(new Interest("Internet of Things"));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        interestRecyclerView.setLayoutManager(layoutManager);
        interestRecyclerView.setAdapter(new InterestAdapter(interestList, new InterestAdapter.OnInterestClickListener() {
            @Override
            public void onInterestClick(int position) {
                interestList.get(position).setSelected(!interestList.get(position).isSelected());
                interestRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }));


    }


    public void redirectToTask(View view) {
        List<String> formattedInterestList=new ArrayList<>();
        for(Interest interest:interestList){
            if(interest.isSelected()){
                formattedInterestList.add(interest.getName());
            }
        }
        if(formattedInterestList.size()>0){
            db.updateUserInterestListById(this,myApp.getCurrentUser().getId(),formattedInterestList);
        }else{
            Toast.makeText(this, "No interests selected", Toast.LENGTH_SHORT).show();
        }
    }
}