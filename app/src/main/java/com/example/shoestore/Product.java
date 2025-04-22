package com.example.shoestore;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int id;
    private boolean isFavorite;
    private String imageURL;
    private double price;
    private long timestamp;
    private String category;
    private String gender;
    private String brand;
    private float Rating;
    private int items_left;

    public Product() {
        // Required empty constructor for Firebase
    }

    public Product(String name, int id, String imageURL, double price, long timestamp,
                   String category, String gender, float Rating, int items_left, String brand) {
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
        this.price = price;
        this.timestamp = timestamp;
        this.category = category;
        this.gender = gender;
        this.Rating = Rating;
        this.items_left = items_left;
        this.brand = brand;
    }

    // Getter and Setter for 'id' as int
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getBrand() {
        return brand;
    }

    public String getImageURL() {
        return imageURL;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCategory() {
        return category;
    }

    public String getGender() {
        return gender;
    }

    public float getRating() {
        return Rating;
    }

    public int getItems_left() {
        return items_left;
    }
}
