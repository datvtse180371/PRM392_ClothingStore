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

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItemWithProductDTO> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItemWithProductDTO item, int newQuantity);
        void onRemoveItem(CartItemWithProductDTO item);
    }

    public CartAdapter(List<CartItemWithProductDTO> cartItems, CartItemListener listener) {
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
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView quantityText;
        private Button decreaseButton;
        private Button increaseButton;
        private Button removeButton;
        private TextView itemTotalText;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cartProductImage);
            productName = itemView.findViewById(R.id.cartProductName);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            quantityText = itemView.findViewById(R.id.quantityText);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            removeButton = itemView.findViewById(R.id.removeButton);
            itemTotalText = itemView.findViewById(R.id.itemTotalText);
        }

        public void bind(CartItemWithProductDTO item, CartItemListener listener) {
            if (item.getProduct() != null) {
                productName.setText(item.getProduct().getName());
                productPrice.setText(String.format("$%.2f", item.getProduct().getPrice()));
                
                // Load product image
                if (item.getProduct().getImageUrl() != null && !item.getProduct().getImageUrl().isEmpty()) {
                    int resId = itemView.getContext().getResources().getIdentifier(
                            item.getProduct().getImageUrl(), "drawable", 
                            itemView.getContext().getPackageName());
                    if (resId != 0) {
                        productImage.setImageResource(resId);
                    } else {
                        productImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                } else {
                    productImage.setImageResource(R.drawable.ic_launcher_background);
                }
            }

            quantityText.setText(String.valueOf(item.getQuantity()));
            
            // Calculate and display item total
            double itemTotal = item.getProduct() != null ? 
                    item.getProduct().getPrice() * item.getQuantity() : 0;
            itemTotalText.setText(String.format("Total: $%.2f", itemTotal));

            // Set up quantity controls
            decreaseButton.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1) {
                    listener.onQuantityChanged(item, currentQuantity - 1);
                }
            });

            increaseButton.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                listener.onQuantityChanged(item, currentQuantity + 1);
            });

            removeButton.setOnClickListener(v -> {
                listener.onRemoveItem(item);
            });
        }
    }
}
