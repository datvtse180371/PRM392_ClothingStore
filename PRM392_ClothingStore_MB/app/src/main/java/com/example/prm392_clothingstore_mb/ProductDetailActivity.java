package com.example.prm392_clothingstore_mb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_clothingstore_mb.api.Cart.CartRepository;
import com.example.prm392_clothingstore_mb.api.Cart.CartService;
import com.example.prm392_clothingstore_mb.model.AddToCartRequest;
import com.example.prm392_clothingstore_mb.model.ApiResponse;
import com.example.prm392_clothingstore_mb.model.ProductDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductDTO product;
    private Button addToCartButton;
    private Button decreaseQuantityButton;
    private Button increaseQuantityButton;
    private TextView quantityText;
    private int selectedQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        product = (ProductDTO) getIntent().getSerializableExtra("product");

        // Find views
        TextView nameText = findViewById(R.id.detailProductName);
        TextView priceText = findViewById(R.id.detailProductPrice);
        TextView categoryText = findViewById(R.id.detailProductCategory);
        TextView stockText = findViewById(R.id.detailProductStock);
        TextView descriptionText = findViewById(R.id.detailProductDescription);
        ImageView productImage = findViewById(R.id.detailProductImage);
        addToCartButton = findViewById(R.id.addToCartButton);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        quantityText = findViewById(R.id.quantityText);

        if (product != null) {
            nameText.setText(product.getName());
            priceText.setText("Price: $" + product.getPrice());
            categoryText.setText("Category: " + (product.getCategory() != null ? product.getCategory() : "-"));
            stockText.setText("Stock: " + product.getStock() + " available");
            descriptionText.setText(product.getDescription() != null ? product.getDescription() : "No description.");

            // Disable add to cart if out of stock
            if (product.getStock() <= 0) {
                addToCartButton.setEnabled(false);
                addToCartButton.setText("Out of Stock");
                decreaseQuantityButton.setEnabled(false);
                increaseQuantityButton.setEnabled(false);
            }

            // Load image (if using Picasso/Glide and product.getImageUrl() is not null)
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                int resId = getResources().getIdentifier(product.getImageUrl(), "drawable", getPackageName());
                if (resId != 0) {
                    productImage.setImageResource(resId);
                } else {
                    productImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                productImage.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // Set up quantity controls
        setupQuantityControls();

        // Set up Add to Cart button
        addToCartButton.setOnClickListener(v -> addToCart());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addToCart() {
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button to prevent multiple clicks
        addToCartButton.setEnabled(false);
        addToCartButton.setText("Adding...");

        AddToCartRequest request = new AddToCartRequest(userId, product.getId(), selectedQuantity);
        CartService cartService = CartRepository.getCartService();
        Call<ApiResponse> call = cartService.addToCart(request);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                addToCartButton.setEnabled(true);
                addToCartButton.setText("Add to Cart");

                Log.d("ProductDetail", "Add to cart response code: " + response.code());
                Log.d("ProductDetail", "API URL: " + call.request().url().toString());

                if (response.isSuccessful()) {
                    Log.d("ProductDetail", "Product added to cart successfully");
                    Toast.makeText(ProductDetailActivity.this, "Product added to cart!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ProductDetail", "Failed to add to cart: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("ProductDetail", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e("ProductDetail", "Error reading error body: " + e.getMessage());
                        }
                    }
                    Toast.makeText(ProductDetailActivity.this, "Failed to add to cart. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                addToCartButton.setEnabled(true);
                addToCartButton.setText("Add to Cart");
                Log.e("ProductDetail", "Error adding to cart: " + t.getMessage());
                Log.e("ProductDetail", "API URL: " + call.request().url().toString());
                t.printStackTrace();
                Toast.makeText(ProductDetailActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupQuantityControls() {
        // Initialize quantity display
        quantityText.setText(String.valueOf(selectedQuantity));

        // Set up decrease button
        decreaseQuantityButton.setOnClickListener(v -> {
            if (selectedQuantity > 1) {
                selectedQuantity--;
                quantityText.setText(String.valueOf(selectedQuantity));
            }
        });

        // Set up increase button
        increaseQuantityButton.setOnClickListener(v -> {
            if (product != null && selectedQuantity < product.getStock()) {
                selectedQuantity++;
                quantityText.setText(String.valueOf(selectedQuantity));
            } else if (product != null) {
                Toast.makeText(this, "Cannot exceed available stock (" + product.getStock() + ")", Toast.LENGTH_SHORT).show();
            }
        });
    }
}