package com.example.task21p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private IUnitConverter converter=new LengthConverter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerMode = findViewById(R.id.spinnerMode);
        bindSpinner(spinnerMode, R.array.mode);
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinnersBasedOnMode(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button convertButton=findViewById(R.id.convertButton);
        EditText initValue=findViewById(R.id.initValue);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double value = Double.parseDouble(initValue.getText().toString());
                Spinner spinnerFromUnit=findViewById(R.id.spinnerFromUnit);
                Spinner spinnerToUnit=findViewById(R.id.spinnerToUnit);
                double calculatedAnswer= converter.convert(value, spinnerFromUnit.getSelectedItem().toString(), spinnerToUnit.getSelectedItem().toString());
                TextView answer=findViewById(R.id.answer);
                answer.setText(String.format("Answer: %.10f", calculatedAnswer));
            }
        });
    }

    private void updateUnitSpinnersBasedOnMode(String mode) {
        int arrayResourceId;
        switch (mode) {
            case "Weight":
                arrayResourceId = R.array.weight_unit;
                converter = new WeightConverter();
                break;
            case "Temperature":
                arrayResourceId = R.array.temperature_unit;
                converter = new TemperatureConverter();
                break;
            default:
                arrayResourceId = R.array.length_unit;
                converter = new LengthConverter();
        }
        bindSpinner(findViewById(R.id.spinnerFromUnit), arrayResourceId);
        bindSpinner(findViewById(R.id.spinnerToUnit), arrayResourceId);
    }

    private void bindSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

