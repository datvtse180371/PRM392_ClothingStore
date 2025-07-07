package com.example.prm392_clothingstore_mb.api.Category;

import com.example.prm392_clothingstore_mb.api.APIClient;

public class CategoryRepository {
    public static CategoryService getCategoryService() {
        return APIClient.getClient().create(CategoryService.class);
    }
}
