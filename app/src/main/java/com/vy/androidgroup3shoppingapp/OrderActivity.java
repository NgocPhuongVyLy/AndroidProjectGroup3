package com.vy.androidgroup3shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private Button btnAll, btnShipped, btnDelivered, btnReturns;
    private ImageButton btnHome, btnCart, btnProfile, btnLogo;
    private TextView tvTitle;
    private ConstraintLayout orderContainer;
    private ArrayList<Order> orderList;
    private ArrayList<Order> filteredOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        btnAll = findViewById(R.id.btn_all);
        btnShipped = findViewById(R.id.btn_shipped);
        btnDelivered = findViewById(R.id.btn_delivered);
        btnReturns = findViewById(R.id.btn_return);
        orderContainer = findViewById(R.id.orderContainer);
        btnHome = findViewById(R.id.btn_home);
        btnCart = findViewById(R.id.btn_cart);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogo = findViewById(R.id.btn_logo);

        //Static order data
        orderList = new ArrayList<>();
        orderList.add(new Order("Order 1","$40.00", "shipped", (new int[] {R.drawable.image1, R.drawable.image7})));
        orderList.add(new Order("Order 2", "$40.00","delivered", (new int[] {R.drawable.image4, R.drawable.image6})));
        orderList.add(new Order("Order 3", "$40.00","returns", (new int[] {R.drawable.image2, R.drawable.image5})));
        orderList.add(new Order("Order 4", "$40.00","shipped", (new int[] {R.drawable.image9, R.drawable.image3})));
        orderList.add(new Order("Order 5", "$40.00","shipped",(new int[] {R.drawable.image3, R.drawable.image1})));
        orderList.add(new Order("Order 6", "$40.00","delivered", (new int[] {R.drawable.image5, R.drawable.image2})));
        orderList.add(new Order("Order 7", "$40.00","returns", (new int[] {R.drawable.image6, R.drawable.image3})));
        orderList.add(new Order("Order 8", "$40.00","shipped", (new int[] {R.drawable.image7, R.drawable.image8})));

        if (orderContainer == null) {
            Log.e("OrderActivity", "orderContainer is null. Check layout ID.");
        }

        if (orderContainer.getVisibility() != View.VISIBLE) {
            Log.e("OrderActivity", "orderContainer is not visible. Setting it to visible.");
            orderContainer.setVisibility(View.VISIBLE);
        }

        // Initialize order list
        filteredOrders = new ArrayList<>(orderList);
        loadAllOrders(filteredOrders);

        // Button listeners
        setupNavigationButtons();
        setupCategoryButtons();

        //Navigation click listener
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, ProductDetailsActivity.class); //replace with cart class when available
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadAllOrders(ArrayList<Order> orderToDisplay) {
        orderContainer.removeAllViews();
        int previousId = ConstraintSet.PARENT_ID; //Attach first order to parent

        for (Order order : orderToDisplay) {
            // Create order view dynamically
            View orderView = getLayoutInflater().inflate(R.layout.activity_orderhistory, orderContainer, false);
            orderView.setId(View.generateViewId());

            ImageView image1 = orderView.findViewById(R.id.orderImage1);
            ImageView image2 = orderView.findViewById(R.id.orderImage2);
            ImageView image3 = orderView.findViewById(R.id.orderImage3);
            TextView orderName = orderView.findViewById(R.id.orderName);
            TextView orderPrice = orderView.findViewById(R.id.orderPrice);

            //Setting image order
            image1.setImageResource(order.getImageResId()[0]); // First image is mandatory
            if (order.getImageResId().length > 1) {
                image2.setVisibility(View.VISIBLE);
                image2.setImageResource(order.getImageResId()[1]);
            }
            if (order.getImageResId().length > 2) {
                image3.setVisibility(View.VISIBLE);
                image3.setImageResource(order.getImageResId()[2]);
            }
            // Setting data
            orderName.setText(order.getId());
            orderPrice.setText(order.getPrice());

            // Ensure the view has no parent
            if (orderView.getParent() != null) {
                ((ViewGroup) orderView.getParent()).removeView(orderView);
            }

            // Add to parent container
            orderContainer.addView(orderView);

            // Constrain it programmatically
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(orderContainer);
            constraintSet.connect(orderView.getId(), ConstraintSet.TOP, previousId, ConstraintSet.BOTTOM, 16);
            constraintSet.connect(orderView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(orderView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.applyTo(orderContainer);

            // Update previousId
            previousId = orderView.getId();
        }
    }

    // Set up button listeners
    private void setupNavigationButtons() {
        findViewById(R.id.btn_home).setOnClickListener(v -> navigateTo("Home"));
        findViewById(R.id.btn_profile).setOnClickListener(v -> navigateTo("Profile"));
        findViewById(R.id.btn_cart).setOnClickListener(v -> navigateTo("Cart"));
        findViewById(R.id.btn_logo).setOnClickListener(v -> navigateTo("Logo"));
    }

    // Filter orders based on button click
    private void setupCategoryButtons() {
        findViewById(R.id.btn_all).setOnClickListener(v -> filterOrders("All Orders"));
        findViewById(R.id.btn_shipped).setOnClickListener(v -> filterOrders("Shipped"));
        findViewById(R.id.btn_delivered).setOnClickListener(v -> filterOrders("Delivered"));
        findViewById(R.id.btn_return).setOnClickListener(v -> filterOrders("Returns"));
    }

    private void navigateTo(String destination) {
        Toast.makeText(this, "Navigating to " + destination, Toast.LENGTH_SHORT).show();
    }

    // Filter orders based on status
    private void filterOrders(String status) {
        filteredOrders.clear(); //clear prev filtering

            if (status.equalsIgnoreCase("All Orders")) {
                filteredOrders = new ArrayList<>(orderList);  // Show all orders
            } else {
                filteredOrders = new ArrayList<>();
                for (Order order : orderList) {
                    if (order.getStatus().equalsIgnoreCase(status)) {
                        filteredOrders.add(order);
                    }
                }
            }
        loadAllOrders(filteredOrders);  // Reload orders after filtering

    }

}
