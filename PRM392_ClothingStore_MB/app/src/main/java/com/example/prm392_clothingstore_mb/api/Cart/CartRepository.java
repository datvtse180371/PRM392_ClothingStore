package com.example.prm392_clothingstore_mb.api.Cart;

import com.example.prm392_clothingstore_mb.api.APIClient;

public class CartRepository {
    public static CartService getCartService() {
        return APIClient.getClient().create(CartService.class);
    }
}
