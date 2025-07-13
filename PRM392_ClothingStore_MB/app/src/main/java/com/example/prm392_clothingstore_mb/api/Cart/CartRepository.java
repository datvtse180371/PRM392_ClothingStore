package com.example.prm392_clothingstore_mb.api.Cart;

import com.example.prm392_clothingstore_mb.api.APIClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartRepository {
    private static CartService cartService;
    public static CartService getCartService() {
        if (cartService == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5221/api/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            cartService = retrofit.create(CartService.class);
        }
        return cartService;
    }
}