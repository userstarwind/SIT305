package com.example.task91p;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.tasks.Task;

import java.text.BreakIterator;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PublishAdvertActivity extends AppCompatActivity {

    private RadioGroup radioTypeGroup;
    private String advertType;
    private EditText publishAdvertName;
    private EditText publishAdvertDescription;
    private EditText publishAdvertPhone;
    private TextView publishAdvertLocation;
    private TextView publishAdvertDate;
    private LocalDate publishAdvertLocalDate;
    private String publishAdvertPlaceName;
    private String publishAdvertPlaceAddress;
    private Double publishAdvertPlaceLatitude;
    private Double publishAdvertPlaceLongitude;
    private ActivityResultLauncher<Intent> launcher;
    private PlacesClient placesClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private AlertDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_advert);
        Toolbar toolbar = findViewById(R.id.publish_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        radioTypeGroup = findViewById(R.id.radio_type_group);
        placesClient = Places.createClient(this);
        final RadioButton radioLostButton = findViewById(R.id.radio_button_lost);
        final RadioButton radioFoundButton = findViewById(R.id.radio_button_found);
        advertType = "Lost";
        publishAdvertName = findViewById(R.id.publish_name_edit_text);
        publishAdvertDescription = findViewById(R.id.publish_description_edit_text);
        publishAdvertPhone = findViewById(R.id.publish_phone_edit_text);
        publishAdvertLocation = findViewById(R.id.selected_location_text_view);
        publishAdvertDate = findViewById(R.id.publish_selected_date_text_view);
        radioTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_lost) {
                    advertType = "Lost";
                } else if (checkedId == R.id.radio_button_found) {
                    advertType = "Found";
                }
            }
        });
        radioLostButton.setChecked(true);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            publishAdvertPlaceName = data.getStringExtra("PLACE_NAME");
                            publishAdvertPlaceAddress = data.getStringExtra("PLACE_ADDRESS");
                            publishAdvertPlaceLatitude = data.getDoubleExtra("PLACE_LATITUDE", 0);
                            publishAdvertPlaceLongitude = data.getDoubleExtra("PLACE_LONGITUDE", 0);
                            publishAdvertLocation.setText("Location: " + publishAdvertPlaceName);
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                    }
                });

    }

    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }

    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }

    public void selectDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(PublishAdvertActivity.this,
                (datePicker, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    publishAdvertLocalDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth);
                    publishAdvertDate.setText("Date: " + publishAdvertLocalDate.toString());
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void publishAdvert(View view) {
        String name = publishAdvertName.getText().toString();
        String description = publishAdvertDescription.getText().toString();
        String phone = publishAdvertPhone.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(publishAdvertPlaceName) || TextUtils.isEmpty(publishAdvertPlaceAddress) ||
                Double.isNaN(publishAdvertPlaceLatitude) || Double.isNaN(publishAdvertPlaceLongitude) ||
                TextUtils.isEmpty(advertType) || publishAdvertLocalDate == null || TextUtils.isEmpty(publishAdvertLocalDate.toString())) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        MyApp myApp = (MyApp) getApplication();
        if (myApp.getDb().insertAdvert(advertType, name, description, phone, publishAdvertLocalDate.toString(), publishAdvertPlaceName, publishAdvertPlaceAddress, publishAdvertPlaceLatitude, publishAdvertPlaceLongitude)) {
            Toast.makeText(this, "Advert added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add advert.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(PublishAdvertActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
        }
    }
    public void getCurrentLocation(View view) {
showLoading(this);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    PlaceLikelihood mostLikelyPlace = getPlaceLikelihood(task);

                    if (mostLikelyPlace != null && mostLikelyPlace.getPlace().getLatLng() != null) {
                        publishAdvertPlaceName = mostLikelyPlace.getPlace().getName();
                        publishAdvertPlaceAddress = mostLikelyPlace.getPlace().getAddress();
                        publishAdvertPlaceLatitude = mostLikelyPlace.getPlace().getLatLng().latitude;
                        publishAdvertPlaceLongitude = mostLikelyPlace.getPlace().getLatLng().longitude;
                        publishAdvertLocation.setText("Location: " + publishAdvertPlaceName);
                    } else {
                        Toast.makeText(PublishAdvertActivity.this, "No valid location found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Toast.makeText(PublishAdvertActivity.this, "Place not found: " + apiException.getStatusCode(), Toast.LENGTH_LONG).show();
                    }
                }
            hideLoading(this);
            });
        } else {
            hideLoading(this);
            getLocationPermission();
        }
    }

    @Nullable
    private static PlaceLikelihood getPlaceLikelihood(Task<FindCurrentPlaceResponse> task) {
        FindCurrentPlaceResponse response = task.getResult();
        PlaceLikelihood mostLikelyPlace = null;
        double maxLikelihood = -1.0;

        if (response != null && response.getPlaceLikelihoods() != null) {
            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                if (placeLikelihood.getPlace().getLatLng() != null && placeLikelihood.getLikelihood() > maxLikelihood) {
                    maxLikelihood = placeLikelihood.getLikelihood();
                    mostLikelyPlace = placeLikelihood;
                }
            }
        }
        return mostLikelyPlace;
    }



    public void selectLocation(View view) {
        Intent intent = new Intent(PublishAdvertActivity.this, MapActivity.class);
        launcher.launch(intent);
    }


}