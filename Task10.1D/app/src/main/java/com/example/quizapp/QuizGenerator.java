package com.example.quizapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizGenerator {
    private QuizApiService apiService;

    public QuizGenerator() {
        apiService = RetrofitClient.getClient().create(QuizApiService.class);
    }
    private AlertDialog progressDialog;
    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }

    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }
    public void fetchQuizQuestions(Activity activity, String topic, Callback<Quiz> callback) {
        showLoading(activity);
        Call<Quiz> call = apiService.getQuiz(topic);
        call.enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                hideLoading(activity);
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Error fetching quiz data", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {
                hideLoading(activity);
                activity.runOnUiThread(() -> Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show());
                callback.onFailure(call, t);
            }
        });
    }
}
