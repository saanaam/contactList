package com.example.sanamyavarpour.contact_list.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

     static  String BASE_URL = "http://hkns.ir/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory (GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
