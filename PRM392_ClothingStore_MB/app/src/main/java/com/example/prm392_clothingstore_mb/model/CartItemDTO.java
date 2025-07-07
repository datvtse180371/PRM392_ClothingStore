package com.example.prm392_clothingstore_mb.model;

import java.util.Date;

public class CartItemDTO {
    private int id;
    private Integer userId;
    private Date addedAt;
    private int quantity;
    private Integer productId;
    public CartItemDTO() {
    }
    public CartItemDTO(int id, Integer userId, Date addedAt, int quantity, Integer productId) {
        this.id = id;
        this.userId = userId;
        this.addedAt = addedAt;
        this.quantity = quantity;
        this.productId = productId;

    }
    public int getId() {
        return id;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Date getAddedAt() {
        return addedAt;
    }
    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
