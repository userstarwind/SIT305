package com.example.task91p;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdvertDetailsActivity extends AppCompatActivity {
    private Advert currentAdvert;
    private MyApp myApp;
    private TextView nameTextView;
    private TextView typeTextView;
    private TextView descriptionTextView;
    private TextView phoneTextView;
    private TextView dateTextView;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);
        Intent intent = getIntent();
        int currentAdvertId = intent.getIntExtra("ADVERT_ID", -1);
        myApp = (MyApp) getApplication();
        Toolbar toolbar=findViewById(R.id.advert_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            Intent backIntent=new Intent(this,BoardActivity.class);
            startActivity(backIntent);
            finish();
        });
        currentAdvert = myApp.getDb().getAdvertById(currentAdvertId);
        nameTextView = findViewById(R.id.advert_details_name);
        typeTextView = findViewById(R.id.advert_details_type);
        descriptionTextView = findViewById(R.id.advert_details_description);
        phoneTextView = findViewById(R.id.advert_details_phone);
        dateTextView = findViewById(R.id.advert_details_date);
        locationTextView = findViewById(R.id.advert_details_location);
        if (currentAdvert != null) {
            nameTextView.setText("Name: " + currentAdvert.getName());
            typeTextView.setText("Type: " + currentAdvert.getType());
            descriptionTextView.setText("Description: " + currentAdvert.getDescription());
            phoneTextView.setText("Contact Phone: " + currentAdvert.getPhone());
            dateTextView.setText("Date: " + currentAdvert.getDate());
            locationTextView.setText("Location: " + currentAdvert.getPlaceAddress());
        } else {
            nameTextView.setText("Name: Not Available");
            typeTextView.setText("Type: Not Available");
            descriptionTextView.setText("Description: Not Available");
            phoneTextView.setText("Phone: Not Available");
            dateTextView.setText("Date: Not Available");
            locationTextView.setText("Location: Not Available");

            Toast.makeText(this, "Advert not found!", Toast.LENGTH_LONG).show();
        }

    }

    public void removeAdvert(View view) {
        myApp.getDb().deleteAdvertById(currentAdvert.getId());
        Intent intent=new Intent(AdvertDetailsActivity.this,BoardActivity.class);
        startActivity(intent);
        finish();
    }
}