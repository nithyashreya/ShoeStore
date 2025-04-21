package com.example.shoestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {
    private RecyclerView productsRecyclerView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Setup Toolbar
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // Connect the listener
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Highlight current tab
        bottomNavigation.setSelectedItemId(R.id.nav_explore);

        // Initialize views
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ProductAdapter(this, productList, false);
        productsRecyclerView.setAdapter(adapter);

        // Setup category chips
        ChipGroup categoryChips = findViewById(R.id.categoryChips);
        categoryChips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Chip chip = group.findViewById(checkedIds.get(0));
            filterProducts(chip.getText().toString());
        });
        bottomNavigation.setSelectedItemId(R.id.nav_explore);
        // Load products
        loadProducts();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.nav_explore) {
            // Already in ExploreActivity
            return true;
        } else if (itemId == R.id.nav_favorite) {
            startActivity(new Intent(this, FavoritesActivity.class));
            finish();
            return true;
        }else if (itemId == R.id.nav_cart) {
//            startActivity(new Intent(this, CartActivity.class));
//            finish();
                return true;
        } else if (itemId == R.id.nav_orders) {
//            startActivity(new Intent(this, OrdersActivity.class));
//            finish();
            return true;
        }
        return false;
    }

    private void loadProducts() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("products");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Product product = childSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ExploreActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterProducts(String category) {
        if (category.equals("All Shoes")) {
            adapter.updateList(productList);
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        adapter.updateList(filteredList);
    }

}