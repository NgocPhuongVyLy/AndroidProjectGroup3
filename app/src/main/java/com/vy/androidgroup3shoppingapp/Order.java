package com.vy.androidgroup3shoppingapp;

public class Order {
    private String id;
    private String price;
    private String status;
    private int[] imageResId; // For drawable resources images


    public Order(String id, String price, String status, int[] imageResId) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.imageResId = imageResId;
    }

    public String getId() { return id; }
    public String getPrice() {return price; }
    public String getStatus() { return status; }
    public int[] getImageResId() { return imageResId; }
}
