package com.example.shoestore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    // UI Elements
    private ImageView productImage;
    private TextView productName, productPrice, productDescription, quantityText, itemsLeft;
    private RatingBar productRating;
    private Button addToCart, buyNow, increaseQty, decreaseQty;

    // Data
    private int quantity = 1;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();
        loadProductFromIntent();
        setupListeners();
    }

    private void initializeViews() {
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productRating = findViewById(R.id.productRating);
        quantityText = findViewById(R.id.quantityText);
        itemsLeft = findViewById(R.id.itemsLeft);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        increaseQty = findViewById(R.id.increaseQty);
        decreaseQty = findViewById(R.id.decreaseQty);
    }

    private void loadProductFromIntent() {
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText("$" + product.getPrice());
            productDescription.setText("Brand: " + product.getBrand());
            productRating.setRating((float) product.getRating()); // load rating from dataset
            itemsLeft.setText("Only " + product.getItems_left() + " left in stock!");
            quantityText.setText(String.valueOf(quantity));

            Glide.with(this).load(product.getImageURL()).into(productImage);
        } else {
            Toast.makeText(this, "Error loading product details.", Toast.LENGTH_SHORT).show();
            finish(); // Go back if no product is received
        }
    }

    private void setupListeners() {
        increaseQty.setOnClickListener(v -> {
            if (quantity < product.getItems_left()) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        decreaseQty.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        addToCart.setOnClickListener(v -> {
            // Add to cart logic
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });

        buyNow.setOnClickListener(v -> {
            // Proceed to payment logic
            Toast.makeText(this, "Proceed to buy", Toast.LENGTH_SHORT).show();
        });
    }
}
