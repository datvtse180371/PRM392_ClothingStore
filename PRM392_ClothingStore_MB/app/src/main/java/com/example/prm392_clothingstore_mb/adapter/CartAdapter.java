package com.example.prm392_clothingstore_mb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.R;
import com.example.prm392_clothingstore_mb.model.CartItemWithProductDTO;
import com.example.prm392_clothingstore_mb.model.ProductDTO;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItemWithProductDTO> cartItems;
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onIncreaseQuantity(CartItemWithProductDTO item);
        void onDecreaseQuantity(CartItemWithProductDTO item);
        void onRemoveItem(CartItemWithProductDTO item);
        void onItemClick(CartItemWithProductDTO item);
    }

    public CartAdapter(List<CartItemWithProductDTO> cartItems, OnCartItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemWithProductDTO item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductPrice, tvQuantity, tvTotalPrice;
        private ImageView ivProductImage;
        private Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            // Set click listeners
            btnIncrease.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onIncreaseQuantity(cartItems.get(position));
                }
            });

            btnDecrease.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDecreaseQuantity(cartItems.get(position));
                }
            });

            btnRemove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRemoveItem(cartItems.get(position));
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(cartItems.get(position));
                }
            });
        }

        public void bind(CartItemWithProductDTO item) {
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            if (item.getProduct() != null) {
                ProductDTO product = item.getProduct();
                tvProductName.setText(product.getName());
                tvProductPrice.setText("$" + product.getPrice());

                // Calculate total price
                double totalPrice = product.getPrice() * item.getQuantity();
                tvTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice));

                // Load product image
                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    int resId = itemView.getContext().getResources().getIdentifier(
                            product.getImageUrl(), "drawable", itemView.getContext().getPackageName());
                    if (resId != 0) {
                        ivProductImage.setImageResource(resId);
                    } else {
                        ivProductImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                } else {
                    ivProductImage.setImageResource(R.drawable.ic_launcher_background);
                }
            } else {
                tvProductName.setText("Unknown Product");
                tvProductPrice.setText("$0.00");
                tvTotalPrice.setText("Total: $0.00");
                ivProductImage.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }
}