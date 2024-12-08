package com.vy.androidgroup3shoppingapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {

    private EditText cardNumber, expiryDate, cvv, nameOnCard;
    private CheckBox saveCardCheckbox;
    private TextView totalAmount;
    private Button btnPayNow;
    private ImageButton apay, ppay, visa, debit;
    private String paymentMethod = "None";
    private MySqlHelper dbHelper;
    private int userId;
    private int orderId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        dbHelper = new MySqlHelper(this);

        // Initialize UI components
        cardNumber = findViewById(R.id.cardNumber);
        expiryDate = findViewById(R.id.expiryDate);
        cvv = findViewById(R.id.cvv);
        nameOnCard = findViewById(R.id.nameOnCard);
        saveCardCheckbox = findViewById(R.id.saveCardCheckbox);
        totalAmount = findViewById(R.id.totalAmount);
        btnPayNow = findViewById(R.id.btnPayNow);
        apay = findViewById(R.id.Apay);
        ppay = findViewById(R.id.ppay);
        visa = findViewById(R.id.visa);
        debit = findViewById(R.id.debit);

        // Retrieve user ID from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("userDetails", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1); // Default value is -1 if not found

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Handle user not logged in scenario (e.g., redirect to login page)
            return;
        }


        // Get the current order ID and total amount based on the user's cart
        loadTotalAmount();

        // Handle payment method selection
        setupPaymentMethodSelection();

        // Handle Pay Now button click
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void setupPaymentMethodSelection() {
        apay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Apple Pay";
                Toast.makeText(Payment.this, "Apple Pay selected", Toast.LENGTH_SHORT).show();
            }
        });

        ppay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "PayPal";
                Toast.makeText(Payment.this, "PayPal selected", Toast.LENGTH_SHORT).show();
            }
        });

        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Visa";
                Toast.makeText(Payment.this, "Visa selected", Toast.LENGTH_SHORT).show();
            }
        });

        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "Debit Card";
                Toast.makeText(Payment.this, "Debit Card selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTotalAmount() {
        Cursor cursor = dbHelper.getCartDetailsForUser(userId);

        if (cursor != null && cursor.getCount() > 0) {
            double subtotal = 0.0;

            while (cursor.moveToNext()) {
                int quantity = cursor.getInt(1);
                double price = cursor.getDouble(2);
                subtotal += quantity * price;
            }

            cursor.close();
            // Calculating shipping fee
            double shippingFee = 8.0;
            // Calculate taxes
            double taxes = (subtotal < 200) ? 9.0 : 13.0;
            // Calculate total including taxes
            double totalIncludingTaxes = subtotal + shippingFee + taxes;
            // Set text for totalAmount TextView
            String totalAmountText = String.format("$%.2f\n$%.2f\n$%.2f\n\n$%.2f", subtotal, shippingFee, taxes, totalIncludingTaxes);
            totalAmount.setText(totalAmountText);

            // Get the current order ID for the user
            String getOrderIdQuery = "SELECT orderid FROM ordertable WHERE userid = ? AND orderstatus = 'Pending'";
            Cursor orderCursor = dbHelper.getReadableDatabase().rawQuery(getOrderIdQuery, new String[]{String.valueOf(userId)});

            if (orderCursor != null && orderCursor.moveToFirst()) {
                orderId = orderCursor.getInt(0);
                orderCursor.close();
            }
        } else {
            Toast.makeText(this, "No items in cart", Toast.LENGTH_SHORT).show();
        }
    }

    private void processPayment() {
        // Validate inputs
        String cardNum = cardNumber.getText().toString().trim();
        String expDate = expiryDate.getText().toString().trim();
        String cvvCode = cvv.getText().toString().trim();
        String cardName = nameOnCard.getText().toString().trim();
        boolean saveCard = saveCardCheckbox.isChecked();

        if (cardNum.isEmpty() || expDate.isEmpty() || cvvCode.isEmpty() || cardName.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (paymentMethod.equals("None")) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save payment details into the database
        ContentValues paymentValues = new ContentValues();
        paymentValues.put("userid", userId);
        paymentValues.put("orderid", orderId);
        paymentValues.put("address", "abc Street Name");
        paymentValues.put("totalamount", Double.parseDouble(totalAmount.getText().toString().replace("$", "")));
        paymentValues.put("paymentmethod", paymentMethod);
        paymentValues.put("paymentstatus", "Successful");
        dbHelper.getWritableDatabase().insert("payment", null, paymentValues);

        // Provide feedback to the user
        Toast.makeText(this, "Payment Successfull", Toast.LENGTH_LONG).show();

        // Confirm data insertion by querying the database
        Cursor paymentCursor = dbHelper.getPaymentDetails(userId, orderId);
        if (paymentCursor != null && paymentCursor.moveToFirst()) {
            String confirmationMessage = "Payment for order " + orderId + " has been successfully recorded.";
            Toast.makeText(this, confirmationMessage, Toast.LENGTH_LONG).show();
            Log.d("Payment", confirmationMessage);
            Log.d("Database", "Inserted payment details: " + paymentValues.toString());
            paymentCursor.close();
        } else {
            Toast.makeText(this, "Error confirming payment details in database.", Toast.LENGTH_SHORT).show();
        }

        // Optionally save card details if checkbox is checked
        if (saveCard) {
            saveCardDetails(cardNum, expDate, cvvCode, cardName);
        }

        // Clear fields after payment
        clearFields();
    }

    private void saveCardDetails(String cardNum, String expDate, String cvvCode, String cardName) {
        // Logic to save card details
        Toast.makeText(this, "Card details saved", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        cardNumber.setText("");
        expiryDate.setText("");
        cvv.setText("");
        nameOnCard.setText("");
        saveCardCheckbox.setChecked(false);
        paymentMethod = "None";
        totalAmount.setText("$0.00");
    }
}
