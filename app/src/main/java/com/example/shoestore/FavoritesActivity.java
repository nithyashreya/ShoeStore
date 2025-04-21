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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView favoritesRecyclerView;
    private ProductAdapter adapter;
    private List<Product> favoriteProducts = new ArrayList<>();

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.nav_explore) {
            startActivity(new Intent(this, ExploreActivity.class));
//            finish();
            return true;
        } else if (itemId == R.id.nav_favorite) {
//
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);



        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, favoriteProducts, false);
        favoritesRecyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // Connect the listener
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Highlight current tab
        bottomNavigation.setSelectedItemId(R.id.nav_favorite);

        loadFavorites();
    }

    private void loadFavorites() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please login to view favorites", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(user.getUid())
                .child("favorites");

        userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteProducts.clear();
                for (DataSnapshot favoriteSnapshot : snapshot.getChildren()) {
                    String productId = favoriteSnapshot.getKey();
                    fetchProductDetails(productId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoritesActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductDetails(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("products")
                .child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    product.setId(Integer.parseInt(productId)); // Convert string to int
                    product.setFavorite(true); // Mark as favorite
                    favoriteProducts.add(product);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FavoritesActivity", "Error loading product: " + productId, error.toException());
            }
        });


    }
}
