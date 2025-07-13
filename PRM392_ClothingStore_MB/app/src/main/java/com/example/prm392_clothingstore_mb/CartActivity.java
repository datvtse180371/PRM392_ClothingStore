package com.example.prm392_clothingstore_mb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.prm392_clothingstore_mb.adapter.CartAdapter;
import com.example.prm392_clothingstore_mb.api.Cart.CartRepository;
import com.example.prm392_clothingstore_mb.api.Cart.CartService;
import com.example.prm392_clothingstore_mb.model.CartItemDTO;
import com.example.prm392_clothingstore_mb.model.CartItemWithProductDTO;
import com.example.prm392_clothingstore_mb.model.CartNotificationDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView tvCartSummary;
    private TextView tvTotalItems;
    private TextView tvUniqueProducts;
    private CartService cartService;
    private Button btnBack, btnCheckout;
    private int userId = 1; // Thay đổi theo user hiện tại
    private List<CartItemWithProductDTO> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize service
        cartService = CartRepository.getCartService();

        // Find views
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        tvCartSummary = findViewById(R.id.tvCartSummary);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvUniqueProducts = findViewById(R.id.tvUniqueProducts);
        btnBack = findViewById(R.id.btnBack);
        btnCheckout = findViewById(R.id.btnCheckout);

        btnBack.setOnClickListener(v -> finish());

        btnCheckout.setOnClickListener(v -> finish());

        // Setup RecyclerView
        setupRecyclerView();

        // Load cart data
        loadCartData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void loadCartData() {
        Call<CartNotificationDTO> call = cartService.getCartNotification(userId);
        call.enqueue(new Callback<CartNotificationDTO>() {
            @Override
            public void onResponse(Call<CartNotificationDTO> call, Response<CartNotificationDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartNotificationDTO cartNotification = response.body();
                    updateUI(cartNotification);
                } else {
                    Log.e("CartActivity", "Failed to load cart data: " + response.message());
                    Toast.makeText(CartActivity.this, "Failed to load cart data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartNotificationDTO> call, Throwable t) {
                Log.e("CartActivity", "Network error: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(CartNotificationDTO cartNotification) {
        // Update summary texts
        tvCartSummary.setText(cartNotification.getMessage());
        tvTotalItems.setText("Total Items: " + cartNotification.getTotalItems());
        tvUniqueProducts.setText("Products: " + cartNotification.getUniqueProducts());

        // Update RecyclerView
        cartItems.clear();
        cartItems.addAll(cartNotification.getCartItems());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIncreaseQuantity(CartItemWithProductDTO item) {
        // Create updated cart item
        CartItemDTO updatedItem = new CartItemDTO();
        updatedItem.setId(item.getId());
        updatedItem.setUserId(item.getUserId());
        updatedItem.setProductId(item.getProductId());
        updatedItem.setQuantity(item.getQuantity() + 1);
        updatedItem.setAddedAt(item.getAddedAt());

        updateCartItem(item.getId(), updatedItem);
    }

    @Override
    public void onDecreaseQuantity(CartItemWithProductDTO item) {
        if (item.getQuantity() > 1) {
            CartItemDTO updatedItem = new CartItemDTO();
            updatedItem.setId(item.getId());
            updatedItem.setUserId(item.getUserId());
            updatedItem.setProductId(item.getProductId());
            updatedItem.setQuantity(item.getQuantity() - 1);
            updatedItem.setAddedAt(item.getAddedAt());

            updateCartItem(item.getId(), updatedItem);
        } else {
            onRemoveItem(item);
        }
    }

    @Override
    public void onRemoveItem(CartItemWithProductDTO item) {
        Call<Void> call = cartService.deleteCartItem(item.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                    loadCartData(); // Refresh data
                } else {
                    Log.e("CartActivity", "Failed to remove item: " + response.message());
                    Toast.makeText(CartActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartActivity", "Network error: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(CartItemWithProductDTO item) {
        // Handle item click (navigate to product detail, etc.)
        if (item.getProduct() != null) {
            Toast.makeText(this, "Clicked: " + item.getProduct().getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartItem(int id, CartItemDTO cartItem) {
        Call<Void> call = cartService.updateCartItem(id, cartItem);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadCartData(); // Refresh data
                } else {
                    Log.e("CartActivity", "Failed to update item: " + response.message());
                    Toast.makeText(CartActivity.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CartActivity", "Network error: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}