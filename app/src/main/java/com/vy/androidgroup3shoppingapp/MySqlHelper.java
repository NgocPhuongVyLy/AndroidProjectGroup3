package com.vy.androidgroup3shoppingapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoppingApp.db";

    // User table creation SQL
    private static final String CREATE_USER_TABLE = "CREATE TABLE User (" +
            "userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT NOT NULL, " +
            "password TEXT NOT NULL, " +
            "email TEXT UNIQUE NOT NULL, " +
            "phone_number TEXT);";

    // Product details table creation SQL
    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE productdetails (" +
            "productid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "product_name TEXT NOT NULL, " +
            "category TEXT NOT NULL, " +
            "details TEXT, " +
            "price DECIMAL NOT NULL, " +
            "image TEXT);"; // Image will store the image name (or path)

    // Order table creation SQL
    private static final String CREATE_ORDER_TABLE = "CREATE TABLE ordertable (" +
            "orderid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "userid INTEGER NOT NULL, " +
            "orderstatus TEXT DEFAULT 'Pending', " +
            "FOREIGN KEY (userid) REFERENCES User(userid));";

    // OrderItems table creation SQL
    private static final String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE orderitems (" +
            "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "orderid INTEGER NOT NULL, " +
            "productid INTEGER NOT NULL, " +
            "quantity INTEGER DEFAULT 1, " +
            "FOREIGN KEY (orderid) REFERENCES ordertable(orderid), " +
            "FOREIGN KEY (productid) REFERENCES productdetails(productid));";

    // Payment table creation SQL
    private static final String CREATE_PAYMENT_TABLE = "CREATE TABLE payment (" +
            "paymentid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "userid INTEGER NOT NULL, " +
            "orderid INTEGER NOT NULL, " +
            "address TEXT NOT NULL, " +
            "totalamount DECIMAL NOT NULL, " +
            "paymentmethod TEXT DEFAULT 'Visa', " +
            "paymentstatus TEXT DEFAULT 'Pending', " +
            "FOREIGN KEY (userid) REFERENCES User(userid), " +
            "FOREIGN KEY (orderid) REFERENCES ordertable(orderid));";

    // Constructor
    public MySqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create all tables
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);
        db.execSQL(CREATE_PAYMENT_TABLE);

        // Insert sample data for testing
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all tables if they exist and create new ones
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS productdetails");
        db.execSQL("DROP TABLE IF EXISTS ordertable");
        db.execSQL("DROP TABLE IF EXISTS orderitems");
        db.execSQL("DROP TABLE IF EXISTS payment");
        onCreate(db);
    }


// Method to insert sample data for testing
    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample user data
        ContentValues userValues = new ContentValues();
        userValues.put("username", "JohnDoe");
        userValues.put("password", "password123");
        userValues.put("email", "johndoe@example.com");
        userValues.put("phone_number", "1234567890");
        long userId = db.insert("User", null, userValues);

        // Insert more sample user data
        ContentValues userValues2 = new ContentValues();
        userValues2.put("username", "JaneDoe");
        userValues2.put("password", "password456");
        userValues2.put("email", "janedoe@example.com");
        userValues2.put("phone_number", "0987654321");
        long userId2 = db.insert("User", null, userValues2);

        // Insert sample product data
        ContentValues productValues1 = new ContentValues();
        productValues1.put("product_name", "Sample Product 1");
        productValues1.put("category", "Electronics");
        productValues1.put("details", "This is a sample product 1.");
        productValues1.put("price", 99.99);
        productValues1.put("image", "sample_image1.jpg");  // Assume the image is stored locally
        long productId1 = db.insert("productdetails", null, productValues1);

        ContentValues productValues2 = new ContentValues();
        productValues2.put("product_name", "Sample Product 2");
        productValues2.put("category", "Home Appliances");
        productValues2.put("details", "This is a sample product 2.");
        productValues2.put("price", 49.99);
        productValues2.put("image", "sample_image2.jpg");  // Assume the image is stored locally
        long productId2 = db.insert("productdetails", null, productValues2);
        Log.d("Database", "Inserted product ID: " + productId1 + ", " + productId2);

        // Insert sample order data
        ContentValues orderValues = new ContentValues();
        orderValues.put("userid", userId);  // Use the userId from the inserted user
        long orderId = db.insert("ordertable", null, orderValues);
        Log.d("Database", "Inserted order ID: " + orderId);

        // Insert sample order item data
        ContentValues orderItemValues1 = new ContentValues();
        orderItemValues1.put("orderid", orderId);
        orderItemValues1.put("productid", productId1);  // Use the productId from the inserted product
        orderItemValues1.put("quantity", 2);  // Example quantity
        long orderItemId1 = db.insert("orderitems", null, orderItemValues1);

        ContentValues orderItemValues2 = new ContentValues();
        orderItemValues2.put("orderid", orderId);
        orderItemValues2.put("productid", productId2);  // Use the second productId
        orderItemValues2.put("quantity", 1);  // Example quantity
        long orderItemId2 = db.insert("orderitems", null, orderItemValues2);

        Log.d("Database", "Inserted order items with IDs: " + orderItemId1 + ", " + orderItemId2);

        // Insert sample payment data
        ContentValues paymentValues = new ContentValues();
        paymentValues.put("userid", userId);
        paymentValues.put("orderid", orderId);
        paymentValues.put("address", "123 Street Name");
        paymentValues.put("totalamount", 199.98);  // 2 products at $99.99 each
        paymentValues.put("paymentmethod", "Visa");
        paymentValues.put("paymentstatus", "Pending");
        db.insert("payment", null, paymentValues);

        Log.d("Database", "Sample data inserted successfully");
    }


    // Method to get cart items for a specific order
    public Cursor getCartItems(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("MySqlHelper", "Fetching cart items for order ID: " + orderId);
        String query = "SELECT p.product_name, oi.quantity, p.price " +
                "FROM orderitems oi " +
                "JOIN productdetails p ON oi.productid = p.productid " +
                "WHERE oi.orderid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
        if (cursor != null) {
            Log.d("MySqlHelper", "Number of items fetched: " + cursor.getCount());
        } else {
            Log.d("MySqlHelper", "Cursor is null");
        }
        return cursor;
    }

    // Method to update the quantity of a cart item
    public void updateQuantity(int orderId, String productName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        db.update("orderitems", values, "orderid = ? AND productid = (SELECT productid FROM productdetails WHERE product_name = ?)",
                new String[]{String.valueOf(orderId), productName});
    }

    // Method to get cart details for a specific user
    @SuppressLint("Range")
    public Cursor getCartDetailsForUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current order ID for the user
        String getOrderIdQuery = "SELECT orderid FROM ordertable WHERE userid = ? AND orderstatus = 'Pending'";
        Cursor orderCursor = db.rawQuery(getOrderIdQuery, new String[]{String.valueOf(userId)});
        int orderId = -1;

        if (orderCursor != null && orderCursor.moveToFirst()) {
            orderId = orderCursor.getInt(orderCursor.getColumnIndex("orderid"));
            orderCursor.close();
        }

        // Get cart items for the order
        if (orderId != -1) {
            return getCartItems(orderId);
        } else {
            Log.d("MySqlHelper", "No pending orders found for user ID: " + userId);
            return null;
        }
    }

    public Cursor getPaymentDetails(int userId, int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM payment WHERE userid = ? AND orderid = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(orderId)});
    }
}
