package com.example.task81c;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AIApiServices {
    @GET("clear_history")
    Call<ServiceResponse> clearHistory();

    @POST("get_new_response")
    Call<ServiceResponse> getNewResponse(@Body RequestBody requestBody);
}
