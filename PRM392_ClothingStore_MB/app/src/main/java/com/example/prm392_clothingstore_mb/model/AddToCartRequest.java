package com.example.prm392_clothingstore_mb.model;

public class AddToCartRequest {
    private int userId;
    private int productId;
    private int quantity;

    public AddToCartRequest() {
        this.quantity = 1; // Default quantity
    }

    public AddToCartRequest(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
