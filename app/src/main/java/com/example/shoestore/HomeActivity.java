package com.example.shoestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView horizontalRecyclerView;
    private RecyclerView gridRecyclerView;
    private ProductAdapter horizontalAdapter;
    private ProductAdapter gridAdapter;
    private List<Product> productList = new ArrayList<>();
    private ChipGroup categoryChips;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ValueEventListener productsListener;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase references
        productsRef = FirebaseDatabase.getInstance().getReference("products");

        setupAuthListener();
        setupUI();
        setupAdapters();
        loadProducts();
    }

    private void setupAuthListener() {
        authStateListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                // Clear data when user logs out
                productList.clear();
                updateAdapters();
            }
        };
    }

    private void setupUI() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);
        gridRecyclerView = findViewById(R.id.recyclerView);
        categoryChips = findViewById(R.id.categoryChips);

        // Setup layout managers
        horizontalRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gridRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Setup chip listener
        categoryChips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                filterProducts(chip.getText().toString());
            }
        });
    }

    private void setupAdapters() {
        horizontalAdapter = new ProductAdapter(this, productList, true);
        gridAdapter = new ProductAdapter(this, productList, false);

        horizontalRecyclerView.setAdapter(horizontalAdapter);
        gridRecyclerView.setAdapter(gridAdapter);
    }

    private void loadProducts() {
        // Remove previous listener to avoid duplicates
        if (productsListener != null) {
            productsRef.removeEventListener(productsListener);
        }

        productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    try {
                        Product product = childSnapshot.getValue(Product.class);
                        if (product != null) {
                            product.setId(Integer.parseInt(childSnapshot.getKey()));
                            // Convert string key to int
                            if (product.getImageURL() != null) {
                                productList.add(product);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("HomeActivity", "Error parsing product", e);
                    }
                }
                // Newest first
                Collections.reverse(productList);
                updateAdapters();
                Log.d("HomeActivity", "Loaded " + productList.size() + " products");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeActivity", "Database error: " + error.getMessage());
                Toast.makeText(HomeActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the listener
        productsRef.orderByChild("timestamp")
                .limitToLast(10)
                .addValueEventListener(productsListener);
    }

    private void updateAdapters() {
        runOnUiThread(() -> {
            horizontalAdapter.notifyDataSetChanged();
            gridAdapter.notifyDataSetChanged();
        });
    }

    private void filterProducts(String category) {
        if (category.equals("All")) {
            horizontalAdapter.updateList(productList);
            gridAdapter.updateList(productList);
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory() != null &&
                    product.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        horizontalAdapter.updateList(filteredList);
        gridAdapter.updateList(filteredList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        if (productsListener != null) {
            productsRef.removeEventListener(productsListener);
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            // Already in HomeActivity
                            return true;
                        case R.id.nav_explore:
                            startActivity(new Intent(HomeActivity.this, ExploreActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_favorite:
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                                overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(HomeActivity.this, "Please log in to view favorites", Toast.LENGTH_SHORT).show();
                            }
                            return true;

                    }
                    return false;
                }
            };
}
