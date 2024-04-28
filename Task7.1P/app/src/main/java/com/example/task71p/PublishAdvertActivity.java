package com.example.task71p;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.time.LocalDate;
import java.util.Objects;

public class PublishAdvertActivity extends AppCompatActivity {
    private RadioGroup radioTypeGroup;
    private String advertType;
    private EditText publishAdvertName;
    private EditText publishAdvertDescription;
    private EditText publishAdvertPhone;
    private EditText publishAdvertLocation;
    private TextView publishAdvertDate;
    private LocalDate publishAdvertLocalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_advert);
        Toolbar toolbar = findViewById(R.id.publish_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        radioTypeGroup = findViewById(R.id.radio_type_group);
        final RadioButton radioLostButton = findViewById(R.id.radio_button_lost);
        final RadioButton radioFoundButton = findViewById(R.id.radio_button_found);
        advertType = "Lost";
        publishAdvertName = findViewById(R.id.publish_name_edit_text);
        publishAdvertDescription = findViewById(R.id.publish_description_edit_text);
        publishAdvertPhone = findViewById(R.id.publish_phone_edit_text);
        publishAdvertLocation = findViewById(R.id.publish_location_edit_text);
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
        String location = publishAdvertLocation.getText().toString();
        if (name.isEmpty() || description.isEmpty() || phone.isEmpty() || location.isEmpty() || advertType.isEmpty() || publishAdvertLocalDate.toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        MyApp myApp = (MyApp) getApplication();
        if (myApp.getDb().insertAdvert(advertType, name, description, phone, publishAdvertLocalDate.toString(), location)) {
            Toast.makeText(this, "Advert added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add advert.", Toast.LENGTH_SHORT).show();
        }
    }
}