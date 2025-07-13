package com.example.prm392_clothingstore_mb.api.Cart;

import com.example.prm392_clothingstore_mb.model.AddToCartRequest;
import com.example.prm392_clothingstore_mb.model.ApiResponse;
import com.example.prm392_clothingstore_mb.model.CartItemDTO;
import com.example.prm392_clothingstore_mb.model.CartNotificationDTO;
import com.example.prm392_clothingstore_mb.model.CheckoutRequest;
import com.example.prm392_clothingstore_mb.model.OrderDTO;
import com.example.prm392_clothingstore_mb.model.UpdateQuantityRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartService {

    // Get cart notification for specific user
    @GET("CartItem/notification/{userId}")
    Call<CartNotificationDTO> getCartNotification(@Path("userId") int userId);

    // Get cart notification for authenticated user (requires JWT token)
    @GET("CartItem/notification")
    Call<CartNotificationDTO> getMyCartNotification(@Header("Authorization") String authToken);

    // Get cart items for specific user
    @GET("CartItem/user/{userId}")
    Call<List<CartItemDTO>> getCartItemsByUserId(@Path("userId") int userId);

    // Get all cart items
    @GET("CartItem")
    Call<List<CartItemDTO>> getAllCartItems();

    // Add product to cart
    @POST("CartItem/add-to-cart")
    Call<ApiResponse> addToCart(@Body AddToCartRequest request);

    // Update cart item quantity
    @PUT("CartItem/update-quantity/{cartItemId}")
    Call<ApiResponse> updateQuantity(@Path("cartItemId") int cartItemId, @Body UpdateQuantityRequest request);

    // Remove item from cart
    @DELETE("CartItem/remove-from-cart/{cartItemId}")
    Call<ApiResponse> removeFromCart(@Path("cartItemId") int cartItemId);

    // Checkout cart
    @POST("CartItem/checkout/{userId}")
    Call<OrderDTO> checkout(@Path("userId") int userId, @Body CheckoutRequest request);
}
