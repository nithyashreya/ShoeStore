package com.example.shoestore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {

    private Context context;
    private List<Shoe> shoeList;

    public ShoeAdapter(Context context, List<Shoe> shoeList) {
        this.context = context;
        this.shoeList = shoeList;
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shoe_item, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Shoe shoe = shoeList.get(position);
        holder.name.setText(shoe.getName());
        holder.brand.setText(shoe.getBrand());
        holder.price.setText("$" + shoe.getPrice());

        // Load image using Glide
        Glide.with(context)
                .load(shoe.getImageURL())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return shoeList.size();
    }

    public static class ShoeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, brand, price;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shoeImage);
            name = itemView.findViewById(R.id.shoeName);
            brand = itemView.findViewById(R.id.shoeBrand);
            price = itemView.findViewById(R.id.shoePrice);
        }
    }
}
