package com.example.shoestore;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private boolean isHorizontal;

    public ProductAdapter(Context context, List<Product> list, boolean isHorizontal) {
        this.context = context;
        this.productList = list;
        this.isHorizontal = isHorizontal;
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

        // Set favorite button state (works for both layouts now)
        holder.favoriteButton.setImageResource(
                product.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
        );
        holder.favoriteButton.setColorFilter(
                ContextCompat.getColor(context,
                        product.isFavorite() ? R.color.red_500 : R.color.gray_500)
        );

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });

        holder.favoriteButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference favRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getUid())
                        .child("favorites")
                        .child(String.valueOf(product.getId()));

                if (product.isFavorite()) {
                    favRef.removeValue(); // Remove from favorites
                    product.setFavorite(false);
                } else {
                    favRef.setValue(true); // Add to favorites
                    product.setFavorite(true);
                }

                notifyItemChanged(holder.getAdapterPosition());
            } else {
                Toast.makeText(context, "Please login to favorite items", Toast.LENGTH_SHORT).show();
            }
        });


        holder.favoriteButton.setOnClickListener(v -> {
            boolean isFavorite = !product.isFavorite();
            product.setFavorite(isFavorite);
            holder.favoriteButton.setImageResource(
                    isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border
            );
            holder.favoriteButton.setColorFilter(
                    ContextCompat.getColor(context,
                            isFavorite ? R.color.red_500 : R.color.gray_500)
            );
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getUid())
                        .child("favorites")
                        .child(String.valueOf(product.getId()));



            if (isFavorite) {
                    userFavoritesRef.setValue(true);
                } else {
                    userFavoritesRef.removeValue();
                }
            }
        });

        if (holder.buyNowButton != null) {
            holder.buyNowButton.setOnClickListener(v -> {
                Toast.makeText(context, "Buy Now: " + product.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
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

            public ProductViewHolder(View itemView, boolean isHorizontal) {
                super(itemView);
                name = itemView.findViewById(R.id.productName);
                price = itemView.findViewById(R.id.productPrice);
                image = itemView.findViewById(R.id.productImage);
                favoriteButton = itemView.findViewById(R.id.favoriteButton); // Now works for both

                if (isHorizontal) {
                    buyNowButton = itemView.findViewById(R.id.buyNowButton);
                }
            }
        }
    }

