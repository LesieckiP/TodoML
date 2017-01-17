package com.soldiersofmobile.todoexpert.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TodoApi {

    @Headers({
            "X-Parse-Revocable-Session: 1"
    })
    @GET("/login")
    Call<UserResponse> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @GET("/classes/Todo")
    Call<TodosResponse> getTodos(@Header("X-Parse-Session-Token") String token);



}
