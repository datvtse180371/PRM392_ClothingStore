package com.example.prm392_clothingstore_mb.api.User;

import com.example.prm392_clothingstore_mb.model.LoginRequest;
import com.example.prm392_clothingstore_mb.model.LoginResponse;
import com.example.prm392_clothingstore_mb.model.UserRequest;
import com.example.prm392_clothingstore_mb.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/User")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);

    @POST("api/User/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
