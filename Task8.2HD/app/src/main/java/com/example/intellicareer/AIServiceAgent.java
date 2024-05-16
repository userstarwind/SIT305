package com.example.intellicareer;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIServiceAgent {
    private AIApiServices apiService;
    private Gson gson;

    public AIServiceAgent() {
        apiService = RetrofitClient.getClient().create(AIApiServices.class);
        gson = new Gson();
    }

    private AlertDialog progressDialog;

    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }

    private static class RequestBodyModelWithOnePara {
        private String content;

        public RequestBodyModelWithOnePara(String content) {
            this.content = content;
        }
    }

    private static class RequestBodyModelWithTwoPara {
        private String content;
        private String question;

        public RequestBodyModelWithTwoPara(String content, String question) {
            this.content = content;
            this.question = question;
        }
    }

    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }

    public void fetchCVKeywords(Activity activity, String content, Callback<ServiceResponse> callback) {
        showLoading(activity);
        String requestBodyJson = gson.toJson(new RequestBodyModelWithOnePara(content));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);
        Call<ServiceResponse> call = apiService.fetchCVKeyWords(requestBody);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                hideLoading(activity);
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Error sending new message", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                hideLoading(activity);
                activity.runOnUiThread(() -> Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show());
                callback.onFailure(call, throwable);
            }
        });
    }

    public void fetchCVEvaluation(Activity activity, String content, Callback<ServiceResponse> callback) {
        showLoading(activity);
        String requestBodyJson = gson.toJson(new RequestBodyModelWithOnePara(content));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);
        Call<ServiceResponse> call = apiService.fetchCVEvaluation(requestBody);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                hideLoading(activity);
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Error sending new message", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                hideLoading(activity);
                activity.runOnUiThread(() -> Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show());
                callback.onFailure(call, throwable);
            }
        });
    }

    public void fetchOtherResponse(Activity activity, String content, String question,Callback<ServiceResponse> callback) {
        showLoading(activity);
        String requestBodyJson = gson.toJson(new RequestBodyModelWithTwoPara(content,question));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);
        Call<ServiceResponse> call = apiService.fetchOtherResponse(requestBody);
        call.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                hideLoading(activity);
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Error sending new message", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable throwable) {
                hideLoading(activity);
                activity.runOnUiThread(() -> Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show());
                callback.onFailure(call, throwable);
            }
        });
    }
}
