package com.example.prm392_clothingstore_mb.api.Product;

import com.example.prm392_clothingstore_mb.api.APIClient;
import com.example.prm392_clothingstore_mb.api.User.RetrofitClient;

public class ProductRepository {
    public static ProductService getProductService() {
        return APIClient.getClient().create(ProductService.class);
    }
}
