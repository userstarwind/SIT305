package com.example.intellicareer;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AIApiServices {
    @POST("/fetch_cv_key_words")
    Call<ServiceResponse> fetchCVKeyWords(@Body RequestBody requestBody);

    @POST("fetch_cv_evaluation")
    Call<ServiceResponse> fetchCVEvaluation(@Body RequestBody requestBody);

    @POST("fetch_other_response")
    Call<ServiceResponse> fetchOtherResponse(@Body RequestBody requestBody);
}
