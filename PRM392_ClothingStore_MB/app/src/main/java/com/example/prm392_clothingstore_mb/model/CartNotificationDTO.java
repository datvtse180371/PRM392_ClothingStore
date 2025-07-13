package com.example.prm392_clothingstore_mb.model;

import java.util.ArrayList;
import java.util.List;

public class CartNotificationDTO {
    private boolean hasItems;
    private int totalItems;
    private int uniqueProducts;
    private String message;
    private List<CartItemWithProductDTO> cartItems;

    public CartNotificationDTO() {
        this.message = "";
        this.cartItems = new ArrayList<>();
    }

    public CartNotificationDTO(boolean hasItems, int totalItems, int uniqueProducts,
                               String message, List<CartItemWithProductDTO> cartItems) {
        this.hasItems = hasItems;
        this.totalItems = totalItems;
        this.uniqueProducts = uniqueProducts;
        this.message = message != null ? message : "";
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
    }

    // Getters and Setters
    public boolean isHasItems() {
        return hasItems;
    }

    public void setHasItems(boolean hasItems) {
        this.hasItems = hasItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getUniqueProducts() {
        return uniqueProducts;
    }

    public void setUniqueProducts(int uniqueProducts) {
        this.uniqueProducts = uniqueProducts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
    }

    public List<CartItemWithProductDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemWithProductDTO> cartItems) {
        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CartNotificationDTO{" +
                "hasItems=" + hasItems +
                ", totalItems=" + totalItems +
                ", uniqueProducts=" + uniqueProducts +
                ", message='" + message + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }
}