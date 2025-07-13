package com.example.prm392_clothingstore_mb.api.Order;

import com.example.prm392_clothingstore_mb.api.APIClient;

public class OrderRepository {
    public static OrderService getOrderService() {
        return APIClient.getClient().create(OrderService.class);
    }
}
