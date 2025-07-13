package com.example.prm392_clothingstore_mb.model;

public class UpdateQuantityRequest {
    private int quantity;

    public UpdateQuantityRequest() {
    }

    public UpdateQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
