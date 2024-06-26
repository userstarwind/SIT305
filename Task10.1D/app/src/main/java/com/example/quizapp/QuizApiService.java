package com.example.quizapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApiService {
    @GET("getQuiz")
    Call<Quiz> getQuiz(@Query("topic") String topic);
}
