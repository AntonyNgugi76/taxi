package com.example.spinach;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static String BASE_URL ="https://authentic-sparrows.herokuapp.com/api/";
    private static Retrofit retrofit;
    private static Gson gson;

    static Retrofit getRetrofitInstance(){
        if (retrofit==null){
            gson= new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }
}
