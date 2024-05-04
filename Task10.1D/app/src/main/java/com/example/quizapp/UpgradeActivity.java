package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;

import com.example.quizapp.viewmodel.CheckoutViewModel;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;


public class UpgradeActivity extends AppCompatActivity {

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private CheckoutViewModel model;

    private Button purchaseStarterButton;
    private Button purchaseIntermidiateButton;
    private Button purchaseAdvancedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUi();

        model = new ViewModelProvider(this).get(CheckoutViewModel.class);
        model.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    }

    private void initializeUi() {

        setContentView(R.layout.activity_upgrade);
        Toolbar toolbar = findViewById(R.id.activity_upgrade_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        purchaseStarterButton = findViewById(R.id.purchase_starter_button);
        purchaseStarterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment(v);
            }
        });
        purchaseIntermidiateButton = findViewById(R.id.purchase_intermediate_button);
        purchaseIntermidiateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment(v);
            }
        });
        purchaseAdvancedButton = findViewById(R.id.purchase_advanced_button);
        purchaseAdvancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment(v);
            }
        });


    }

    private void setGooglePayAvailable(boolean available) {
        if (!available) {
            Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    public void requestPayment(View view) {

        purchaseStarterButton.setClickable(false);
        purchaseIntermidiateButton.setClickable(false);
        purchaseAdvancedButton.setClickable(false);
        long dummyPriceCents = 100;
        long shippingCostCents = 900;
        long totalPriceCents = dummyPriceCents + shippingCostCents;
        final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);

        AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case AppCompatActivity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case AppCompatActivity.RESULT_CANCELED:
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status);
                        break;
                }

                purchaseStarterButton.setClickable(true);
                purchaseIntermidiateButton.setClickable(true);
                purchaseAdvancedButton.setClickable(true);
        }
    }

    private void handlePaymentSuccess(@Nullable PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(@Nullable Status status) {
        String errorString = "Unknown error.";
        if (status != null) {
            int statusCode = status.getStatusCode();
            errorString = String.format(Locale.getDefault(), "Error code: %d", statusCode);
        }

        Log.e("loadPaymentData failed", errorString);
    }
}