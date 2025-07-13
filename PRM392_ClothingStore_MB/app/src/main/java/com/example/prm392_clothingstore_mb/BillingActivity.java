package com.example.prm392_clothingstore_mb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_clothingstore_mb.api.Cart.CartRepository;
import com.example.prm392_clothingstore_mb.api.Cart.CartService;
import com.example.prm392_clothingstore_mb.model.CheckoutRequest;
import com.example.prm392_clothingstore_mb.model.OrderDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingActivity extends AppCompatActivity {
    private TextView totalAmountText;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private RadioGroup paymentMethodGroup;
    private EditText cardNumberEditText;
    private EditText expiryDateEditText;
    private EditText cvvEditText;
    private Button placeOrderButton;
    
    private double totalAmount;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // Get user ID and total amount
        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        totalAmount = getIntent().getDoubleExtra("total_amount", 0.0);

        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupPaymentMethodListener();
        prefillUserInfo();
    }

    private void initViews() {
        totalAmountText = findViewById(R.id.billingTotalAmount);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        totalAmountText.setText(String.format("Total Amount: $%.2f", totalAmount));

        placeOrderButton.setOnClickListener(v -> processPayment());
    }

    private void setupPaymentMethodListener() {
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            View cardDetailsSection = findViewById(R.id.cardDetailsSection);
            if (checkedId == R.id.creditCardRadio) {
                cardDetailsSection.setVisibility(View.VISIBLE);
            } else {
                cardDetailsSection.setVisibility(View.GONE);
            }
        });
    }

    private void prefillUserInfo() {
        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "");
        String userEmail = prefs.getString("user_email", "");
        
        nameEditText.setText(userName);
        emailEditText.setText(userEmail);
    }

    private void processPayment() {
        if (!validateInputs()) {
            return;
        }

        String paymentMethod = getSelectedPaymentMethod();

        // Show loading state
        placeOrderButton.setEnabled(false);
        placeOrderButton.setText("Processing...");

        // Create checkout request
        CheckoutRequest request = new CheckoutRequest(paymentMethod);

        // Log request details
        Log.d("BillingActivity", "Starting checkout for userId: " + userId);
        Log.d("BillingActivity", "Payment method: " + paymentMethod);
        Log.d("BillingActivity", "Total amount: " + totalAmount);

        CartService cartService = CartRepository.getCartService();
        Call<OrderDTO> call = cartService.checkout(userId, request);

        call.enqueue(new Callback<OrderDTO>() {
            @Override
            public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                placeOrderButton.setEnabled(true);
                placeOrderButton.setText("Place Order");

                Log.d("BillingActivity", "Checkout Response Code: " + response.code());
                Log.d("BillingActivity", "API URL: " + call.request().url().toString());

                if (response.isSuccessful() && response.body() != null) {
                    OrderDTO order = response.body();
                    Log.d("BillingActivity", "Order created successfully. Order ID: " + order.getId());
                    showOrderSuccess(order);
                } else {
                    Log.e("BillingActivity", "Checkout failed: " + response.code());

                    // Get detailed error information
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("BillingActivity", "Error body: " + errorBody);
                            showError("Payment failed: " + errorBody);
                        } catch (Exception e) {
                            Log.e("BillingActivity", "Error reading error body: " + e.getMessage());
                            showError("Payment processing failed. Code: " + response.code());
                        }
                    } else {
                        showError("Payment processing failed. Code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDTO> call, Throwable t) {
                placeOrderButton.setEnabled(true);
                placeOrderButton.setText("Place Order");
                Log.e("BillingActivity", "Checkout error: " + t.getMessage());
                Log.e("BillingActivity", "API URL: " + call.request().url().toString());
                t.printStackTrace();
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }

        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("Address is required");
            return false;
        }

        if (phoneEditText.getText().toString().trim().isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return false;
        }

        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }

        // Validate card details if credit card is selected
        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedPaymentId == R.id.creditCardRadio) {
            if (cardNumberEditText.getText().toString().trim().isEmpty()) {
                cardNumberEditText.setError("Card number is required");
                return false;
            }
            if (expiryDateEditText.getText().toString().trim().isEmpty()) {
                expiryDateEditText.setError("Expiry date is required");
                return false;
            }
            if (cvvEditText.getText().toString().trim().isEmpty()) {
                cvvEditText.setError("CVV is required");
                return false;
            }
        }

        return true;
    }

    private String getSelectedPaymentMethod() {
        int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedId);
        return selectedRadio != null ? selectedRadio.getText().toString() : "Credit Card";
    }

    private void showOrderSuccess(OrderDTO order) {
        Toast.makeText(this, "Order placed successfully! Order ID: " + order.getId(), Toast.LENGTH_LONG).show();
        
        // Navigate back to home and clear cart
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
