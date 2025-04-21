package com.example.shoestore;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*; // or Firestore
import java.util.*;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView horizontalRecyclerView;
    private HorizontalProductAdapter horizontalAdapter;
    private List<Product> horizontalProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize horizontal RecyclerView
        horizontalRecyclerView = findViewById(R.id.horizontalRecyclerView);

        // Setup horizontal layout
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManager);

        // Initialize adapter
        horizontalAdapter = new HorizontalProductAdapter(this, horizontalProductList);
        horizontalRecyclerView.setAdapter(horizontalAdapter);

        // Load horizontal products
        loadHorizontalProducts();
    }

    private void loadHorizontalProducts() {
        DatabaseReference featuredRef = FirebaseDatabase.getInstance().getReference("featured_products");
        featuredRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalProductList.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    Product product = productSnap.getValue(Product.class);
                    horizontalProductList.add(product);
                }
                horizontalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, "Failed to load featured products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
