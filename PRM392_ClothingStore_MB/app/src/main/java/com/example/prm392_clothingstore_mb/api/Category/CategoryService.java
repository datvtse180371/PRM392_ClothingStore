package com.example.prm392_clothingstore_mb.api.Category;

import com.example.prm392_clothingstore_mb.model.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("Category") // Adjust endpoint as per your backend
    Call<List<CategoryDTO>> getAllCategories();
}
