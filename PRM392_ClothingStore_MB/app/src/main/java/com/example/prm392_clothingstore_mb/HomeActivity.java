package com.example.prm392_clothingstore_mb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Display welcome message with user's name
        SharedPreferences prefs = getSharedPreferences("ClothingStorePrefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "User");
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome to Clothing Store, " + userName + "!");

        // Logout button
        MaterialButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // Clear SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Navigate to LoginActivity
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}