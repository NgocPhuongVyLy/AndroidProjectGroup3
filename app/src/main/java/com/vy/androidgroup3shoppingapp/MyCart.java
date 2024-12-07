package com.vy.androidgroup3shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyCart extends AppCompatActivity {

    private MySqlHelper dbHelper;
    private TextView totalAmountTextView;
    private LinearLayout cartLayout;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        dbHelper = new MySqlHelper(this);
        cartLayout = findViewById(R.id.cartLayout);
        totalAmountTextView = findViewById(R.id.totalAmount);

        int userId = 1; // Replace with the actual user ID for testing
        loadCartItems(cartLayout, userId);
//        // Retrieve user ID from shared preferences
//        SharedPreferences sharedPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
//        int userId = sharedPref.getInt("userId", -1); // Default value is -1 if not found
//
//        if (userId != -1) {
//            loadCartItems(cartLayout, userId);
//        } else {
//            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
//            // Handle user not logged in scenario (e.g., redirect to login page)
//        }
//    }


        // Set OnClickListener for the checkout button

        // Find the button in your layout
        btnCheckout = findViewById(R.id.btnCheckout);
        // Check if btnCheckout is null
        if (btnCheckout == null) {
            Log.e("MyCart", "btnCheckout is null. Check your layout file.");
            return;
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyCart", "Checkout button clicked");
                // Create an Intent to start the Payment activity
                Intent intent = new Intent(MyCart.this, Payment.class);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems(LinearLayout cartLayout, int userId) {
        Cursor cursor = dbHelper.getCartDetailsForUser(userId);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No items in cart", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalAmount = 0.0;

        while (cursor.moveToNext()) {
            String productName = cursor.getString(0);
            final int[] quantity = {cursor.getInt(1)};
            double price = cursor.getDouble(2);

            totalAmount += quantity[0] * price;

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(10, 10, 10, 10);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ImageView productImage = new ImageView(this);
            productImage.setImageResource(R.drawable.image_product1);  // Replace with actual image
            productImage.setLayoutParams(new LinearLayout.LayoutParams(
                    200, 200));
            itemLayout.addView(productImage);

            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);
            textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView nameText = new TextView(this);
            nameText.setText(productName);
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            nameText.setTextColor(Color.BLACK);
            nameText.setTypeface(null, android.graphics.Typeface.BOLD);
            textLayout.addView(nameText);

            TextView quantityText = new TextView(this);
            quantityText.setText("Quantity: " + quantity[0]);
            quantityText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            quantityText.setTextColor(Color.GRAY);
            textLayout.addView(quantityText);

            TextView priceText = new TextView(this);
            priceText.setText("$" + price);
            priceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            priceText.setTextColor(Color.parseColor("#FF5722"));
            priceText.setTypeface(null, android.graphics.Typeface.BOLD);
            textLayout.addView(priceText);

            itemLayout.addView(textLayout);

            LinearLayout buttonLayout = new LinearLayout(this);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

            Button decreaseButton = new Button(this);
            decreaseButton.setText("-");
            decreaseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            decreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity[0] > 1) {
                        quantity[0]--;
                        quantityText.setText("Quantity: " + quantity[0]);
                        dbHelper.updateQuantity(userId, productName, quantity[0]);
                        updateTotalAmount();
                    }
                }
            });
            buttonLayout.addView(decreaseButton);

            Button increaseButton = new Button(this);
            increaseButton.setText("+");
            increaseButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            increaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity[0]++;
                    quantityText.setText("Quantity: " + quantity[0]);
                    dbHelper.updateQuantity(userId, productName, quantity[0]);
                    updateTotalAmount();
                }
            });
            buttonLayout.addView(increaseButton);

            itemLayout.addView(buttonLayout);

            cartLayout.addView(itemLayout);
        }

        cursor.close();
        totalAmountTextView.setText("Total: $" + String.format("%.2f", totalAmount));
    }

    private void updateTotalAmount() {
        double totalAmount = 0.0;
        for (int i = 0; i < cartLayout.getChildCount(); i++) {
            LinearLayout itemLayout = (LinearLayout) cartLayout.getChildAt(i);
            LinearLayout textLayout = (LinearLayout) itemLayout.getChildAt(1);
            TextView quantityText = (TextView) textLayout.getChildAt(1);
            TextView priceText = (TextView) textLayout.getChildAt(2);

            int quantity = Integer.parseInt(quantityText.getText().toString().replace("Quantity: ", ""));
            double price = Double.parseDouble(priceText.getText().toString().replace("$", ""));
            totalAmount += quantity * price;
        }

        totalAmountTextView.setText("Total: $" + String.format("%.2f", totalAmount));
    }
}
