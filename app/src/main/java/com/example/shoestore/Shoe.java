package com.example.shoestore;

public class Shoe {
    private int id;
    private String name;
    private String brand;
    private String gender;
    private String category;
    private double price;
    private boolean isInInventory;
    private int itemsLeft;
    private String imageURL;
    private String slug;

    public Shoe() {
        // Default constructor for Firebase
    }

    public Shoe(int id, String name, String brand, String gender, String category, double price, boolean isInInventory, int itemsLeft, String imageURL, String slug) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.gender = gender;
        this.category = category;
        this.price = price;
        this.isInInventory = isInInventory;
        this.itemsLeft = itemsLeft;
        this.imageURL = imageURL;
        this.slug = slug;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getGender() { return gender; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isInInventory() { return isInInventory; }
    public int getItemsLeft() { return itemsLeft; }
    public String getImageURL() { return imageURL; }
    public String getSlug() { return slug; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setGender(String gender) { this.gender = gender; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setInInventory(boolean isInInventory) { this.isInInventory = isInInventory; }
    public void setItemsLeft(int itemsLeft) { this.itemsLeft = itemsLeft; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }
    public void setSlug(String slug) { this.slug = slug; }
}
