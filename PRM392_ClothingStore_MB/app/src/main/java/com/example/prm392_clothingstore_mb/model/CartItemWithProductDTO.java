package com.example.prm392_clothingstore_mb.model;

import java.util.Date;

public class CartItemWithProductDTO {
    private int id;
    private Integer userId;
    private Date addedAt;
    private int quantity;
    private Integer productId;
    private ProductDTO product;

    public CartItemWithProductDTO() {
    }

    public CartItemWithProductDTO(int id, Integer userId, Date addedAt, int quantity, Integer productId, ProductDTO product) {
        this.id = id;
        this.userId = userId;
        this.addedAt = addedAt;
        this.quantity = quantity;
        this.productId = productId;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
