package com.example.prm392_clothingstore_mb.api.Payment;

import com.example.prm392_clothingstore_mb.model.PaymentDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
public interface PaymentService {

    @GET("Payment/{id}")
    Call<PaymentDTO> getPaymentById(@Path("id") int id);

    @GET("Payment")
    Call<List<PaymentDTO>> getAllPayments();

    @POST("Payment")
    Call<Void> addPayment(@Body PaymentDTO payment);

    @PUT("Payment/{id}")
    Call<Void> updatePayment(@Path("id") int id, @Body PaymentDTO payment);

    @DELETE("Payment/{id}")
    Call<Void> deletePayment(@Path("id") int id);
}