package com.example.task91p;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Arrays;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private GoogleMap googleMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private String selectPlaceName;
    private String selectPlaceAddress;
    private Double selectPlaceLatitude;
    private Double selectPlaceLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.map_toolbar);
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            Places.deinitialize();
            finish();
        });
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null && autocompleteFragment.getView() != null) {
            autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        }
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng selectedPlaceLatLng = place.getLatLng();
                if (selectedPlaceLatLng != null && googleMap != null) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions()
                            .position(selectedPlaceLatLng)
                            .title(place.getName()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceLatLng, 15));
                    selectPlaceName=place.getName();
                    selectPlaceAddress=place.getAddress();
                    if(selectPlaceAddress==null){
                        selectPlaceAddress=place.getName();
                    }
                    selectPlaceLatitude=place.getLatLng().latitude;
                    selectPlaceLongitude=place.getLatLng().longitude;
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapActivity.this, "An error occurred: " + status, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    public void saveLocation(View view) {
        Intent resultIntent=new Intent();
        resultIntent.putExtra("PLACE_NAME", selectPlaceName);
        resultIntent.putExtra("PLACE_ADDRESS", selectPlaceAddress);
        resultIntent.putExtra("PLACE_LATITUDE", selectPlaceLatitude);
        resultIntent.putExtra("PLACE_LONGITUDE", selectPlaceLongitude);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}