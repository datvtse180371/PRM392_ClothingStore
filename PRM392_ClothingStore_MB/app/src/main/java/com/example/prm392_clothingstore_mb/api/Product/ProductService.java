package com.example.prm392_clothingstore_mb.api.Product;


import com.example.prm392_clothingstore_mb.model.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    // GET /api/product
    @GET("Product")
    Call<List<ProductDTO>> getAllProducts();
    @GET("Product/all")
    Call<List<ProductDTO>> getProducts(
            @Query("search") String search,
            @Query("categoryId") Integer categoryId
    );

    // GET /api/product/{id}
    @GET("Product/{id}")
    Call<ProductDTO> getProductById(@Path("id") int id);

    // POST /api/product
    @POST("Product")
    Call<ProductDTO> addProduct(@Body ProductDTO product);

    // PUT /api/product/{id}
    @PUT("Product/{id}")
    Call<Void> updateProduct(@Path("id") int id, @Body ProductDTO product);

    // DELETE /api/product/{id}
    @DELETE("Product/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}
