package com.example.prm392_clothingstore_mb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.adapter.OrderAdapter;
import com.example.prm392_clothingstore_mb.api.Order.OrderRepository;
import com.example.prm392_clothingstore_mb.api.Order.OrderService;
import com.example.prm392_clothingstore_mb.model.OrderDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private TextView emptyOrdersText;
    private List<OrderDTO> orders;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Get user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        loadOrders();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.ordersRecyclerView);
        emptyOrdersText = findViewById(R.id.emptyOrdersText);
        orders = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new OrderAdapter(orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        Log.d("OrderHistory", "Loading orders for userId: " + userId);
        OrderService orderService = OrderRepository.getOrderService();
        Call<List<OrderDTO>> call = orderService.getOrdersByUserId(userId);

        call.enqueue(new Callback<List<OrderDTO>>() {
            @Override
            public void onResponse(Call<List<OrderDTO>> call, Response<List<OrderDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderDTO> orderList = response.body();
                    Log.d("OrderHistory", "Orders loaded successfully. Count: " + orderList.size());

                    orders.clear();
                    orders.addAll(orderList);
                    adapter.notifyDataSetChanged();
                    updateUI();
                } else {
                    Log.e("OrderHistory", "Failed to load orders: " + response.code());
                    showError("Failed to load orders");
                }
            }

            @Override
            public void onFailure(Call<List<OrderDTO>> call, Throwable t) {
                Log.e("OrderHistory", "Error loading orders: " + t.getMessage());
                showError("Error loading orders");
            }
        });
    }

    private void updateUI() {
        if (orders.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyOrdersText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyOrdersText.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
