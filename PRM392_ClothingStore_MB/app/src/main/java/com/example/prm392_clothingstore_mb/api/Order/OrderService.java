package com.example.prm392_clothingstore_mb.api.Order;

import com.example.prm392_clothingstore_mb.model.OrderDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {

    // Get all orders
    @GET("Order")
    Call<List<OrderDTO>> getAllOrders();

    // Get order by ID
    @GET("Order/{id}")
    Call<OrderDTO> getOrderById(@Path("id") int id);

    // Get orders by user ID
    @GET("Order/user/{userId}")
    Call<List<OrderDTO>> getOrdersByUserId(@Path("userId") int userId);
}
