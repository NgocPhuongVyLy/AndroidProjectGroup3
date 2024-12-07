package com.vy.androidgroup3shoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MySqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
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
            "image TEXT, " +
            "size TEXT, " +
            "brand TEXT);"; // Image will store the image name (or path)

    // Order table creation SQL
    private static final String CREATE_ORDER_TABLE = "CREATE TABLE ordertable (" +
            "orderid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "userid INTEGER NOT NULL, " +
            "orderstatus TEXT DEFAULT 'Pending', " +
            "FOREIGN KEY (userid) REFERENCES User(userid));";

    // OrderItems table creation SQL (New Table)
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If upgrading to a newer version, handle database schema changes
        if (oldVersion < 2) {
            // Add 'size' column in case it's missing in older versions
            db.execSQL("ALTER TABLE productdetails ADD COLUMN size TEXT;");
            db.execSQL("ALTER TABLE productdetails ADD COLUMN brand TEXT;");
        }
        // Drop all tables if they exist and create new ones
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS productdetails");
        db.execSQL("DROP TABLE IF EXISTS ordertable");
        db.execSQL("DROP TABLE IF EXISTS orderitems"); // Drop OrderItems table if exists
        db.execSQL("DROP TABLE IF EXISTS payment");
        onCreate(db);
    }

    // NPVL DB
    // Main Screen
    public void insertSampleProducts() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if products already exist in the database
        Cursor cursor = db.rawQuery("SELECT * FROM productdetails", null);
        if (cursor.getCount() == 0) {
            // Insert products if database is empty
            ContentValues values = new ContentValues();
            values.put("product_name", "White hooded jacket");
            values.put("category", "Women’s Clothing");
            values.put("details", "100% Cotton");
            values.put("price", 20.99);
            values.put("image", "image_product1.jpeg");
            values.put("size", "M");
            values.put("brand", "Roots");
            db.insert("productdetails", null, values);
            Log.d("MySqlHelper", "Inserted product: White hooded jacket");

            values.clear();
            values.put("product_name", "Party Dress");
            values.put("category", "Women’s Clothing");
            values.put("details", "Elegant evening dress");
            values.put("price", 149.99);
            values.put("image", "image_product5.jpeg");
            values.put("size", "S");
            values.put("brand", "H&M");
            db.insert("productdetails", null, values);
            Log.d("MySqlHelper", "Inserted product: Party Dress");

            // Add two new products
            values.clear();
            values.put("product_name", "Leather Jacket");
            values.put("category", "Men’s Clothing");
            values.put("details", "Genuine leather, stylish and comfortable");
            values.put("price", 250.00);
            values.put("image", "image_product3.jpeg");
            values.put("size", "L");
            values.put("brand", "Levi’s");
            db.insert("productdetails", null, values);
            Log.d("MySqlHelper", "Inserted product: Leather Jacket");

            values.clear();
            values.put("product_name", "Running Shoes");
            values.put("category", "Sportswear");
            values.put("details", "Comfortable running shoes for all types of athletes");
            values.put("price", 99.99);
            values.put("image", "image_product4.jpg");
            values.put("size", "8");
            values.put("brand", "Nike");
            db.insert("productdetails", null, values);
            Log.d("MySqlHelper", "Inserted product: Running Shoes");
        }
        Log.d("MySqlHelper", "Total products after insertion: " + cursor.getCount());

        cursor.close();
        db.close();
    }


    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM productdetails";
        Cursor cursor = db.rawQuery(query, null);
        Log.d("MySqlHelper", "Fetched products: " + productList);

        Log.d("MySqlHelper", "Number of products fetched: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productid")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("details")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("size")),
                        cursor.getString(cursor.getColumnIndexOrThrow("brand"))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("MySqlHelper", "Products Count: " + productList.size());
        return productList;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query;

        if (category.equals("All")) {
            query = "SELECT * FROM productdetails";
        } else {
            query = "SELECT * FROM productdetails WHERE category = ?";
        }

        Cursor cursor = db.rawQuery(query, category.equals("All") ? null : new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productid")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("details")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("size")),
                        cursor.getString(cursor.getColumnIndexOrThrow("brand"))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }

    //SEARCH
    public List<Product> getProductsBySearchQuery(String query) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM productdetails WHERE product_name LIKE ? OR details LIKE ?";
        String wildcardQuery = "%" + query + "%";
        Cursor cursor = db.rawQuery(sql, new String[]{wildcardQuery, wildcardQuery});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productid")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("details")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("size")),
                        cursor.getString(cursor.getColumnIndexOrThrow("brand"))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }



    // NPVL DB



}
