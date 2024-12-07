package com.vy.androidgroup3shoppingapp;

public class Product {
    private int id;
    private String name;
    private String category;
    private String details;
    private double price;
    private String image;
    private String size;
    private String brand;

    public Product(int id, String name, String category, String details, double price, String image, String size, String brand) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.details = details;
        this.price = price;
        this.image = image;
        this.size = size;
        this.brand = brand;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDetails() { return details; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public String getSize() { return size; }
    public String getBrand() { return brand; }
}