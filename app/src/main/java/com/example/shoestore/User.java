package com.example.shoestore;

public class User {
    private String email;
    private String name;
    private String address;

    // Constructor
    public User(String email, String name, String address) {
        this.email = email;
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
