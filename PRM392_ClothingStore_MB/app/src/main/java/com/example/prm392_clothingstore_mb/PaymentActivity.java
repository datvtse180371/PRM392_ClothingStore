package com.example.prm392_clothingstore_mb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_clothingstore_mb.api.Payment.PaymentRepository;
import com.example.prm392_clothingstore_mb.api.Payment.PaymentService;
import com.example.prm392_clothingstore_mb.model.PaymentDTO;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private EditText etCardNumber, etCardHolder, etExpiry, etCVV;
    private Button btnConfirmPayment;
    private double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get total from intent
        totalAmount = getIntent().getDoubleExtra("totalAmount", 0);

        // Initialize views
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolder = findViewById(R.id.etCardHolder);
        etExpiry = findViewById(R.id.etExpiry);
        etCVV = findViewById(R.id.etCVV);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Set total amount
        tvTotalAmount.setText("$" + String.format("%.2f", totalAmount));

        btnConfirmPayment.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String cardHolder = etCardHolder.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();

        if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng PaymentDTO dựa trên class bạn cung cấp
        PaymentDTO payment = new PaymentDTO();
        payment.setOrderId(null); // Nếu chưa có Order, có thể để null
        payment.setPaymentMethod("Credit Card"); // hoặc lấy từ input nếu có
        payment.setPaymentDate(new Date());
        payment.setStatus("Paid");

        PaymentService paymentService = PaymentRepository.getPaymentService();
        paymentService.addPayment(payment).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                    finish(); // hoặc chuyển sang màn hình xác nhận
                } else {
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
