package com.vy.androidgroup3shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    private TextView tvName;
    private ImageButton btnSupport, btnSettings, tvHome, tvProfile, tvCart, tvLogo;
    private Button btnEdit, btnLogout, btnTrack, btnMessages,
            btnOrders, btnReviews, btnCoupon, btnCrbal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        // Initialize views
        tvName = findViewById(R.id.tv_name);
        tvHome = findViewById(R.id.btn_home);
        tvProfile = findViewById(R.id.btn_profile);
        tvCart = findViewById(R.id.btn_cart);
        tvLogo = findViewById(R.id.btn_logo);

        btnSupport = findViewById(R.id.btn_support);
        btnSettings = findViewById(R.id.btn_settings);
        btnEdit = findViewById(R.id.btn_Edit);
        btnLogout = findViewById(R.id.btn_logout);
        btnTrack = findViewById(R.id.btn_track);
//        btnAddress = findViewById(R.id.btn_address);
        btnMessages = findViewById(R.id.btn_messages);
        btnOrders = findViewById(R.id.btn_orders);
        btnReviews = findViewById(R.id.btn_reviews);
        btnCoupon = findViewById(R.id.btn_coupon);
        btnCrbal = findViewById(R.id.btn_crbal);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String username = sharedPreferences.getString("username", "Guest");
            tvName.setText(username);
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            tvName.setText("Guest");
        }
        // Set up listeners for the buttons
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Order button is clicked");
                Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
                startActivity(intent);            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProductDetailsActivity.class); //replace with cart class when available
                startActivity(intent);
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        tvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        btnEdit.setOnClickListener(view -> showUnderDevelopment("Edit"));
        btnReviews.setOnClickListener(view -> showUnderDevelopment("Reviews"));
        btnCoupon.setOnClickListener(view -> showUnderDevelopment("Coupon"));
        btnCrbal.setOnClickListener(view -> showUnderDevelopment("Credit Balance"));
        btnTrack.setOnClickListener(view -> showUnderDevelopment("Tracking"));
//        btnAddress.setOnClickListener(view -> showAddress());

    }

    private  void showUnderDevelopment(String featureName) {
        Toast.makeText(this, featureName + " is under development", Toast.LENGTH_LONG).show();
    }

//    private void showAddress() {
//        String address = "123, Humber College Avenue, Toronto";
//        Toast.makeText(this, "Address: " + address, Toast.LENGTH_LONG).show();
//    }
}
