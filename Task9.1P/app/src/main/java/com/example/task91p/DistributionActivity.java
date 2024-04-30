package com.example.task91p;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

public class DistributionActivity extends AppCompatActivity {
    private List<Advert> advertList;
    private MyApp myApp;
    private MapView mapView;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);
        myApp = (MyApp) getApplication();
        Toolbar toolbar = findViewById(R.id.distribution_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        mapView = findViewById(R.id.distribution_map_view);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;

                advertList = myApp.getDb().getAllAdverts();
                for (Advert advert : advertList) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(advert.getPlaceLatitude(),advert.getPlaceLongitude())).title(advert.getName()));
                }

                if (!advertList.isEmpty()) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(advertList.get(0).getPlaceLatitude(),advertList.get(0).getPlaceLongitude()), 10));
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}