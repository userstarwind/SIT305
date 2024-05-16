package com.example.task81c;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import com.google.gson.Gson;
public class AIServiceAgent {
    private AIApiServices apiService;
    private Gson gson;
    public AIServiceAgent() {
        apiService = RetrofitClient.getClient().create(AIApiServices.class);
        gson=new Gson();
    }
    private AlertDialog progressDialog;
    private void showLoading(Activity activity) {
        activity.runOnUiThread(() -> progressDialog = MuskUtil.showProgressDialog(activity));
    }
    private static class RequestBodyModel {
        private String content;

        public RequestBodyModel(String content) {
            this.content = content;
        }
    }
    private void hideLoading(Activity activity) {
        if (progressDialog != null) {
            activity.runOnUiThread(() -> progressDialog.dismiss());
        }
    }
    public void sendNewMessage(Activity activity, String message, Callback<ServiceResponse> callback) {
        showLoading(activity);
        String requestBodyJson = gson.toJson(new RequestBodyModel(message));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);
        Call<ServiceResponse> call = apiService.getNewResponse(requestBody);
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

    public void clearHistory(Activity activity,Callback<ServiceResponse> callback){
        showLoading(activity);
        Call<ServiceResponse> call= apiService.clearHistory();
        call.enqueue(new Callback<ServiceResponse>(){

            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                hideLoading(activity);
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    activity.runOnUiThread(() -> Toast.makeText(activity, "Error when clearing history", Toast.LENGTH_SHORT).show());
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
