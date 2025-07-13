package com.example.prm392_clothingstore_mb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.R;
import com.example.prm392_clothingstore_mb.model.OrderItemDTO;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<OrderItemDTO> orderItems;

    public OrderItemAdapter(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItemDTO orderItem = orderItems.get(position);
        holder.bind(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameText;
        private TextView quantityText;
        private TextView unitPriceText;
        private TextView subTotalText;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameText);
            quantityText = itemView.findViewById(R.id.quantityText);
            unitPriceText = itemView.findViewById(R.id.unitPriceText);
            subTotalText = itemView.findViewById(R.id.subTotalText);
        }

        public void bind(OrderItemDTO orderItem) {
            // Display product name if available, otherwise show product ID
            if (orderItem.getProduct() != null && orderItem.getProduct().getName() != null) {
                productNameText.setText(orderItem.getProduct().getName());
            } else {
                productNameText.setText("Product ID: " + orderItem.getProductId());
            }

            quantityText.setText("Qty: " + orderItem.getQuantity());
            unitPriceText.setText(String.format("$%.2f", orderItem.getUnitPrice()));

            double subTotal = orderItem.getSubTotal() != null ?
                    orderItem.getSubTotal() :
                    orderItem.getQuantity() * orderItem.getUnitPrice();
            subTotalText.setText(String.format("$%.2f", subTotal));
        }
    }
}
