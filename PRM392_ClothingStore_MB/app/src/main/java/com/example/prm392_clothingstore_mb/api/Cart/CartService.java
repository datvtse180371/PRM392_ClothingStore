package com.example.prm392_clothingstore_mb.api.Cart;

import com.example.prm392_clothingstore_mb.model.CartItemDTO;
import com.example.prm392_clothingstore_mb.model.CartNotificationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface CartService {
    @GET("CartItem/{id}")
    Call<CartItemDTO> getCartItemById(@Path("id") int id);

    @GET("CartItem")
    Call<List<CartItemDTO>> getAllCartItems();

    @POST("CartItem")
    Call<Void> addCartItem(@Body CartItemDTO cartItem);

    @PUT("CartItem/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body CartItemDTO cartItem);

    @DELETE("CartItem/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);

    @GET("CartItem/user/{userId}")
    Call<List<CartItemDTO>> getCartItemsByUserId(@Path("userId") int userId);

    @GET("CartItem/notification/{userId}")
    Call<CartNotificationDTO> getCartNotification(@Path("userId") int userId);

    @GET("CartItem/notification")
    Call<CartNotificationDTO> getMyCartNotification(@Header("Authorization") String bearerToken);
}

