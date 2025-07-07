package com.example.prm392_clothingstore_mb.model;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    private int id;
    private Integer userId;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private List<OrderItemDTO> orderItems;
    private List<PaymentDTO> payments;
    public OrderDTO() {
    }
    public OrderDTO(int id, Integer userId, Date orderDate, double totalAmount, String status, List<OrderItemDTO> orderItems, List<PaymentDTO> payments) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderItems = orderItems;
        this.payments = payments;
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
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public List<PaymentDTO> getPayments() {
        return payments;
    }
    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }
}
