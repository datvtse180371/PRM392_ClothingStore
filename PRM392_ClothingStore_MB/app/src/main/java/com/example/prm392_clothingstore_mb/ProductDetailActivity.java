package com.example.prm392_clothingstore_mb;

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
import com.example.prm392_clothingstore_mb.model.CartItemDTO;
import com.example.prm392_clothingstore_mb.model.ProductDTO;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductDTO product;
    private int quantity = 1;
    private CartService cartService;
    private int userId = 1; // Thay đổi theo user hiện tại

    // Views
    private TextView nameText;
    private TextView priceText;
    private TextView categoryText;
    private TextView descriptionText;
    private TextView tvQuantity;
    private ImageView productImage;
    private Button btnBack;
    private Button btnAddToCart;
    private Button btnDecreaseQuantity;
    private Button btnIncreaseQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        cartService = CartRepository.getCartService();

        // Get product from intent
        product = (ProductDTO) getIntent().getSerializableExtra("product");

        // Initialize views
        initViews();

        // Setup click listeners
        setupClickListeners();

        // Display product information
        displayProductInfo();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        nameText = findViewById(R.id.detailProductName);
        priceText = findViewById(R.id.detailProductPrice);
        categoryText = findViewById(R.id.detailProductCategory);
        descriptionText = findViewById(R.id.detailProductDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        productImage = findViewById(R.id.detailProductImage);
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Quantity controls
        btnDecreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityDisplay();
            }
        });

        btnIncreaseQuantity.setOnClickListener(v -> {
            quantity++;
            updateQuantityDisplay();
        });

        // Add to cart button
        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void displayProductInfo() {
        if (product != null) {
            nameText.setText(product.getName());
            priceText.setText("Price: $" + product.getPrice());
            categoryText.setText("Category: " + (product.getCategory() != null ? product.getCategory() : "-"));
            descriptionText.setText(product.getDescription() != null ? product.getDescription() : "No description.");

            // Load image
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
    }

    private void updateQuantityDisplay() {
        tvQuantity.setText(String.valueOf(quantity));
    }

    private void addToCart() {
        if (product == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button to prevent multiple clicks
        btnAddToCart.setEnabled(false);
        btnAddToCart.setText("Adding...");

        // Create cart item
        CartItemDTO cartItem = new CartItemDTO();
        cartItem.setUserId(userId);
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(quantity);
        cartItem.setAddedAt(new Date()); // Hoặc format theo yêu cầu

        // Add to cart via API
        Call<Void> call = cartService.addCartItem(cartItem);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Re-enable button
                btnAddToCart.setEnabled(true);
                btnAddToCart.setText("Add to Cart");

                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this,
                            quantity + " item(s) added to cart successfully!",
                            Toast.LENGTH_SHORT).show();

                    // Reset quantity to 1
                    quantity = 1;
                    updateQuantityDisplay();
                } else {
                    Log.e("ProductDetailActivity", "Failed to add to cart: " + response.message());
                    Toast.makeText(ProductDetailActivity.this,
                            "Failed to add to cart: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Re-enable button
                btnAddToCart.setEnabled(true);
                btnAddToCart.setText("Add to Cart");

                Log.e("ProductDetailActivity", "Network error: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}