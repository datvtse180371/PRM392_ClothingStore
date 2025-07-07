package com.example.prm392_clothingstore_mb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_clothingstore_mb.adapter.ProductAdapter;
import com.example.prm392_clothingstore_mb.api.Category.CategoryRepository;
import com.example.prm392_clothingstore_mb.api.Category.CategoryService;
import com.example.prm392_clothingstore_mb.api.Product.ProductRepository;
import com.example.prm392_clothingstore_mb.api.Product.ProductService;
import com.example.prm392_clothingstore_mb.model.CategoryDTO;
import com.example.prm392_clothingstore_mb.model.ProductDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ProductAdapter adapter;
    private List<ProductDTO> allProducts = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Spinner categorySpinner;
    private List<CategoryDTO> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private Integer selectedCategoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categorySpinner);

        fetchCategories();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedCategoryId = null; // "All" selected
                } else {
                    selectedCategoryId = categoryList.get(position - 1).getId();
                }
                fetchProducts(searchView.getQuery().toString(), selectedCategoryId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        adapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchProducts(null, null);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchProducts(query, selectedCategoryId);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchProducts(newText, selectedCategoryId);
                return true;
            }
        });

        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome to Clothing Store, " + userName + "!");
    }

    private void fetchProducts(String search, Integer categoryId) {
        ProductService service = ProductRepository.getProductService();
        Call<List<ProductDTO>> call = service.getProducts(search, categoryId);
        call.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                Log.d("API", "URL: " + call.request().url().toString());
                Log.d("API", "Code: " + response.code() + ", Message: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    allProducts = response.body();
                    Collections.sort(allProducts, Comparator.comparing(
                            ProductDTO::getCategory, Comparator.nullsLast(Integer::compareTo)
                    ));
                    adapter.updateList(allProducts);
                } else {
                    allProducts = new ArrayList<>();
                    adapter.updateList(allProducts);
                    showToast("No products found or server error.");
                }
            }
            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                allProducts = new ArrayList<>();
                adapter.updateList(allProducts);
                showToast("Failed to fetch products. Please check your connection.");
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }

    private void fetchCategories() {
        CategoryService service = CategoryRepository.getCategoryService();
        Call<List<CategoryDTO>> call = service.getAllCategories();
        call.enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    categoryNames.add("All Categories");
                    for (CategoryDTO category : categoryList) {
                        categoryNames.add(category.getName());
                    }
                    categoryAdapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                showToast("Failed to load categories.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
