package com.example.spinach;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InterfaceAPI {
    @POST("login")
    Call<String> login(@Header("token") String authToken);
}
