package com.example.prm392_clothingstore_mb.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static String baseURL = "http://10.0.2.2:5221/api/";
    private static Retrofit retrofit;

    public  static  Retrofit getClient(){
        if(retrofit == null){
            // Create Gson with custom date format to handle backend date format
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                    .create();

            retrofit = new Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return  retrofit;
    }
}
