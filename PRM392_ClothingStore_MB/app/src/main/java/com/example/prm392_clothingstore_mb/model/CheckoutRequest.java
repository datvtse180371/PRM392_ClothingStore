package com.example.prm392_clothingstore_mb.model;

public class CheckoutRequest {
    private String paymentMethod;

    public CheckoutRequest() {
        this.paymentMethod = "Credit Card"; // Default payment method
    }

    public CheckoutRequest(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
