package com.example.prm392_clothingstore_mb.model;

import java.io.Serializable;
import java.util.List;

public class ProductDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private Integer category;
    private String size;
    private String color;
    private String imageUrl;
    private List<CartItemDTO> cartItems;
    private CategoryDTO categoryNavigation;
    private List<OrderItemDTO> orderItems;
    public ProductDTO() {
    }
    public ProductDTO(int id, String name, String description, double price, Integer category, String size, String color, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.size = size;
        this.color = color;
        this.imageUrl = imageUrl;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Integer getCategory() {
        return category;
    }
    public void setCategory(Integer category) {
        this.category = category;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public CategoryDTO getCategoryNavigation() {
        return categoryNavigation;
    }
    public void setCategoryNavigation(CategoryDTO categoryNavigation) {
        this.categoryNavigation = categoryNavigation;
    }
}
