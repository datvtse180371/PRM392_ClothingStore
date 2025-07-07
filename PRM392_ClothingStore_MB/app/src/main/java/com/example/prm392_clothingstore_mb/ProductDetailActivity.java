package com.example.prm392_clothingstore_mb;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_clothingstore_mb.model.ProductDTO;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ProductDTO product = (ProductDTO) getIntent().getSerializableExtra("product");

        // Find views
        TextView nameText = findViewById(R.id.detailProductName);
        TextView priceText = findViewById(R.id.detailProductPrice);
        TextView categoryText = findViewById(R.id.detailProductCategory);
        TextView descriptionText = findViewById(R.id.detailProductDescription);
        ImageView productImage = findViewById(R.id.detailProductImage);

        if (product != null) {
            nameText.setText(product.getName());
            priceText.setText("Price: $" + product.getPrice());
            categoryText.setText("Category: " + (product.getCategory() != null ? product.getCategory() : "-"));
            descriptionText.setText(product.getDescription() != null ? product.getDescription() : "No description.");

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}