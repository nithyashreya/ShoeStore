package com.example.shoestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    // UI Elements
    private ImageView productImage;
    private ImageButton favoriteButton;
    private TextView productName, productPrice, productDescription;
    private TextView itemsLeft, quantityText;
    private RatingBar productRating;
    private MaterialButton addToCart, buyNow;
    private Button increaseQty, decreaseQty;

    // Data
    private int quantity = 1;
    private Product product;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews(); // Initialize views FIRST
        adapter = new ProductAdapter(this, new ArrayList<>(), false);
        loadProductFromIntent(); // Then load product data
        setupListeners(); // Finally set up listeners

        // Initialize adapter AFTER product is loaded

    }

    private void initializeViews() {
        productImage = findViewById(R.id.productImage);
        favoriteButton = findViewById(R.id.favoriteButton);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        itemsLeft = findViewById(R.id.itemsLeft);
        quantityText = findViewById(R.id.quantityText);
        productRating = findViewById(R.id.productRating);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);
        increaseQty = findViewById(R.id.increaseQty);
        decreaseQty = findViewById(R.id.decreaseQty);
    }

    private void loadProductFromIntent() {
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            // Basic product info
            productName.setText(product.getName());
            productPrice.setText(getString(R.string.price_format, product.getPrice()));
            productDescription.setText(getString(R.string.product_description_format, product.getBrand()));

            // Stock information
            itemsLeft.setText(getString(R.string.items_left_format, product.getItems_left()));
            quantityText.setText(String.valueOf(quantity));

            productRating.setRating(product.getRating());

            // Load product image
            Glide.with(this)
                    .load(product.getImageURL())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(productImage);

            // Setup favorite button AFTER product is loaded and views are initialized
            adapter.setupFavoriteButton(favoriteButton, product);
        } else {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        // Quantity controls
        increaseQty.setOnClickListener(v -> {
            if (quantity < product.getItems_left()) {
                quantity++;
                quantityText.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, R.string.max_quantity_reached, Toast.LENGTH_SHORT).show();
            }
        });

        decreaseQty.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        // Add to cart
        addToCart.setOnClickListener(v -> {
            Toast.makeText(this,
                    getString(R.string.added_to_cart_message, quantity, product.getName()),
                    Toast.LENGTH_SHORT).show();
        });

        // Buy now
        buyNow.setOnClickListener(v -> {
            Toast.makeText(this, R.string.proceeding_to_checkout, Toast.LENGTH_SHORT).show();
        });
    }
}