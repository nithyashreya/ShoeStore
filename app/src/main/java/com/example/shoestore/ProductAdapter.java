package com.example.shoestore;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private boolean isHorizontal;

    private DatabaseReference favoritesRef;
    private ValueEventListener favoritesListener;
    public ProductAdapter(Context context, List<Product> list, boolean isHorizontal) {
        this.context = context;
        this.productList = list;
        this.isHorizontal = isHorizontal;

        // Initialize Firebase reference
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            favoritesRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(user.getUid())
                    .child("favorites");

            setupFavoritesListener();
        }
    }

    private void setupFavoritesListener() {
        favoritesListener = favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Update all products' favorite status
                for (Product product : productList) {
                    boolean isFavorite = snapshot.child(String.valueOf(product.getId())).exists();
                    product.setFavorite(isFavorite);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductAdapter", "Favorites listener cancelled", error.toException());
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        // Clean up listener when adapter is no longer needed
        if (favoritesRef != null && favoritesListener != null) {
            favoritesRef.removeEventListener(favoritesListener);
        }
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = isHorizontal ? R.layout.item_product_horizontal : R.layout.item_product;
        View view = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        return new ProductViewHolder(view, isHorizontal);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("₹" + product.getPrice());

        Glide.with(context)
                .load(product.getImageURL())
                .error(R.drawable.error_image)
                .into(holder.image);

        updateFavoriteButton(holder.favoriteButton, product.isFavorite());

        if (holder.productRatingText != null) { // If using TextView for rating
            holder.productRatingText.setText(String.format("%.1f", product.getRating()));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

        holder.favoriteButton.setOnClickListener(v -> toggleFavorite(product, holder.favoriteButton));

        if (holder.buyNowButton != null) {
            holder.buyNowButton.setOnClickListener(v -> {
                Toast.makeText(context, "Buy Now: " + product.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void toggleFavorite(Product product, ImageButton button) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(context, "Please login to favorite items", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isFavorite = !product.isFavorite();
        product.setFavorite(isFavorite);
        updateFavoriteButton(button, isFavorite);

        DatabaseReference productRef = favoritesRef.child(String.valueOf(product.getId()));
        if (isFavorite) {
            productRef.setValue(true);
        } else {
            productRef.removeValue();
        }
    }

    private void updateFavoriteButton(ImageButton button, boolean isFavorite) {
        button.setImageResource(
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
        button.setColorFilter(
                ContextCompat.getColor(context,
                        isFavorite ? R.color.red_500 : R.color.gray_500)
        );
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void setupFavoriteButton(ImageButton button, Product product) {
        // Set initial state
        button.setImageResource(
                product.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
        button.setColorFilter(
                ContextCompat.getColor(context,
                        product.isFavorite() ? R.color.red_500 : R.color.gray_500)
        );

        // Set click listener
        button.setOnClickListener(v -> {
            boolean isFavorite = !product.isFavorite();
            product.setFavorite(isFavorite);

            // Update UI
            button.setImageResource(
                    isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
            );
            button.setColorFilter(
                    ContextCompat.getColor(context,
                            isFavorite ? R.color.red_500 : R.color.gray_500)
            );

            // Update Firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getUid())
                        .child("favorites")
                        .child(String.valueOf(product.getId()));

                if (isFavorite) {
                    ref.setValue(true);
                } else {
                    ref.removeValue();
                }
            } else {
                Toast.makeText(context, "Please login to favorite items", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }



        public static class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView name, price;
            ImageView image;
            ImageButton favoriteButton;
            TextView buyNowButton;
            TextView productRatingText;
            public ProductViewHolder(View itemView, boolean isHorizontal) {
                super(itemView);
                name = itemView.findViewById(R.id.productName);
                price = itemView.findViewById(R.id.productPrice);
                image = itemView.findViewById(R.id.productImage);
                productRatingText = itemView.findViewById(R.id.productRatingText);
                favoriteButton = itemView.findViewById(R.id.favoriteButton); // Now works for both

                if (isHorizontal) {
                    buyNowButton = itemView.findViewById(R.id.buyNowButton);
                }
            }
        }
    }

