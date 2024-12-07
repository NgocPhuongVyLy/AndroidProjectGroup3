package com.vy.androidgroup3shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        // Initialize the back icon
        backIcon = findViewById(R.id.imageBackIcon);

        // Set up the click listener for the back icon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity (MainActivity or whatever the parent activity is)
                onBackPressed();  // This is the standard method to go back to the previous activity
            }
        });
        // Get references to the UI elements
        ImageView productImage = findViewById(R.id.imageProductItem);
        TextView productName = findViewById(R.id.twProductName);
        TextView productDescription = findViewById(R.id.tvProductDescription);
        TextView productPrice = findViewById(R.id.tvProductPrice);
        TextView productSize = findViewById(R.id.tvProductSize);
        TextView productBrand = findViewById(R.id.tvProductBrand);

        // Get data from the Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String price = intent.getStringExtra("price");
        int imageResId = intent.getIntExtra("imageResId", R.drawable.image1);
        String size = intent.getStringExtra("size"); // Get size
        String brand = intent.getStringExtra("brand"); // Get brand

        Log.d("ProductDetailsActivity", "Size: " + size + ", Brand: " + brand);
        Log.d("ProductDetailsActivity", "Received data: Name=" + name + ", Description=" + description);
        // Populate the UI with the product details
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(price);
        Glide.with(this).load(imageResId).into(productImage);
        productSize.setText(size);
        productBrand.setText(brand);
    }
}