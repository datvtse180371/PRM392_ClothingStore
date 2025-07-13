package com.example.prm392_clothingstore_mb.api.Payment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class PaymentRepository {
    private static PaymentService paymentService;

    public static PaymentService getPaymentService() {
        if (paymentService == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Điều chỉnh theo định dạng ngày backend trả về
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5221/api/") // đảm bảo đây là baseUrl đúng
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            paymentService = retrofit.create(PaymentService.class);
        }
        return paymentService;
    }
}
