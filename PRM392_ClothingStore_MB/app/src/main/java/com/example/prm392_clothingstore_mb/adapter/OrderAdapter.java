package com.example.prm392_clothingstore_mb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.R;
import com.example.prm392_clothingstore_mb.model.OrderDTO;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderDTO> orders;
    private SimpleDateFormat dateFormat;

    public OrderAdapter(List<OrderDTO> orders) {
        this.orders = orders;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDTO order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIdText;
        private TextView orderDateText;
        private TextView orderStatusText;
        private TextView orderTotalText;
        private RecyclerView orderItemsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            orderItemsRecyclerView = itemView.findViewById(R.id.orderItemsRecyclerView);
        }

        public void bind(OrderDTO order) {
            orderIdText.setText("Order #" + order.getId());
            orderDateText.setText(order.getOrderDate() != null ? 
                    dateFormat.format(order.getOrderDate()) : "N/A");
            orderStatusText.setText(order.getStatus());
            orderTotalText.setText(String.format("$%.2f", order.getTotalAmount()));

            // Set status color
            switch (order.getStatus().toLowerCase()) {
                case "pending":
                    orderStatusText.setTextColor(itemView.getContext().getColor(android.R.color.holo_orange_dark));
                    break;
                case "completed":
                    orderStatusText.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
                    break;
                case "cancelled":
                    orderStatusText.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    orderStatusText.setTextColor(itemView.getContext().getColor(android.R.color.black));
                    break;
            }

            // Setup order items RecyclerView
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                OrderItemAdapter orderItemAdapter = new OrderItemAdapter(order.getOrderItems());
                orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                orderItemsRecyclerView.setAdapter(orderItemAdapter);
                orderItemsRecyclerView.setVisibility(View.VISIBLE);
            } else {
                orderItemsRecyclerView.setVisibility(View.GONE);
            }
        }
    }
}
