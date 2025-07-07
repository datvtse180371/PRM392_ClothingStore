package com.example.prm392_clothingstore_mb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.R;
import com.example.prm392_clothingstore_mb.model.ProductDTO;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductDTO> productList;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(ProductDTO product);
    }

    public ProductAdapter(List<ProductDTO> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDTO product = productList.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<ProductDTO> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, category;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            category = itemView.findViewById(R.id.productCategory);
        }

        void bind(final ProductDTO product, final OnProductClickListener listener) {
            name.setText(product.getName());
            price.setText("Price: $" + product.getPrice());
            category.setText("Category: " + product.getCategory());
            itemView.setOnClickListener(v -> listener.onProductClick(product));
        }
    }
}

