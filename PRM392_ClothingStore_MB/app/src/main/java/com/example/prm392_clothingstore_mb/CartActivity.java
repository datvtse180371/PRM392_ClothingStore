package com.example.prm392_clothingstore_mb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.adapter.CartAdapter;
import com.example.prm392_clothingstore_mb.api.Cart.CartRepository;
import com.example.prm392_clothingstore_mb.api.Cart.CartService;
import com.example.prm392_clothingstore_mb.model.ApiResponse;
import com.example.prm392_clothingstore_mb.model.CartItemWithProductDTO;
import com.example.prm392_clothingstore_mb.model.CartNotificationDTO;
import com.example.prm392_clothingstore_mb.model.UpdateQuantityRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView totalAmountText;
    private TextView emptyCartText;
    private Button checkoutButton;
    private List<CartItemWithProductDTO> cartItems = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Get user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();

        loadCartItems();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountText = findViewById(R.id.totalAmountText);
        emptyCartText = findViewById(R.id.emptyCartText);
        checkoutButton = findViewById(R.id.checkoutButton);

        checkoutButton.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                Intent intent = new Intent(this, BillingActivity.class);
                intent.putExtra("total_amount", calculateTotalAmount());
                startActivity(intent);
            }
        });

    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(cartItems, new CartAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged(CartItemWithProductDTO item, int newQuantity) {
                updateCartItemQuantity(item, newQuantity);
            }

            @Override
            public void onRemoveItem(CartItemWithProductDTO item) {
                removeCartItem(item);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadCartItems() {
        Log.d("CartActivity", "Loading cart items for userId: " + userId);
        CartService cartService = CartRepository.getCartService();
        Call<CartNotificationDTO> call = cartService.getCartNotification(userId);

        call.enqueue(new Callback<CartNotificationDTO>() {
            @Override
            public void onResponse(Call<CartNotificationDTO> call, Response<CartNotificationDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartNotificationDTO notification = response.body();
                    Log.d("CartActivity", "Cart loaded successfully. Items: " + notification.getTotalItems());

                    cartItems.clear();
                    cartItems.addAll(notification.getCartItems());
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Log.e("CartActivity", "Failed to load cart items: " + response.code());
                    showError("Failed to load cart items");
                }
            }

            @Override
            public void onFailure(Call<CartNotificationDTO> call, Throwable t) {
                Log.e("CartActivity", "Error loading cart items: " + t.getMessage());
                showError("Error loading cart items");
            }
        });
    }

    private void updateCartItemQuantity(CartItemWithProductDTO item, int newQuantity) {
        Log.d("CartActivity", "Updating cart item quantity - ID: " + item.getId() + ", New quantity: " + newQuantity);

        // Create update request
        UpdateQuantityRequest request = new UpdateQuantityRequest(newQuantity);

        CartService cartService = CartRepository.getCartService();
        Call<ApiResponse> call = cartService.updateQuantity(item.getId(), request);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("CartActivity", "Cart item quantity updated successfully");
                    // Update local data only after successful API call
                    item.setQuantity(newQuantity);
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Log.e("CartActivity", "Failed to update cart item quantity: " + response.code());
                    showError("Failed to update quantity");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("CartActivity", "Error updating cart item quantity: " + t.getMessage());
                showError("Error updating quantity");
            }
        });
    }

    private void removeCartItem(CartItemWithProductDTO item) {
        Log.d("CartActivity", "Removing cart item - ID: " + item.getId());

        CartService cartService = CartRepository.getCartService();
        Call<ApiResponse> call = cartService.removeFromCart(item.getId());

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("CartActivity", "Cart item removed successfully");
                    // Remove from local data only after successful API call
                    cartItems.remove(item);
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Log.e("CartActivity", "Failed to remove cart item: " + response.code());
                    showError("Failed to remove item");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("CartActivity", "Error removing cart item: " + t.getMessage());
                showError("Error removing item");
            }
        });
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
            totalAmountText.setText("Total: $0.00");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);
            checkoutButton.setEnabled(true);
            totalAmountText.setText(String.format("Total: $%.2f", calculateTotalAmount()));
        }
    }

    private double calculateTotalAmount() {
        double total = 0;
        for (CartItemWithProductDTO item : cartItems) {
            if (item.getProduct() != null) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return total;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems(); // Refresh cart when returning to this activity
    }
}
