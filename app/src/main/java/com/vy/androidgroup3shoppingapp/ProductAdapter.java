package com.vy.androidgroup3shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context context;
    private List<Product> productList;
    //search
    private List<Product> filteredList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        //search
        this.filteredList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //Product product = productList.get(position);
        Product product = filteredList.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDetails());
        holder.productPrice.setText("$" + product.getPrice());

        // Sanitize the image name
        String imageName = product.getImage();
        if (imageName.contains(" ")) {
            imageName = imageName.replace(" ", "_"); // Replace spaces with underscores
        }
        if (imageName.contains(".")) {
            imageName = imageName.substring(0, imageName.lastIndexOf('.')); // Remove the file extension
        }

        // Load image from drawable
        int imageResource = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        if (imageResource != 0) {
            Glide.with(context)
                    .load(imageResource)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.image_product1); // Default image fallback
            Log.w("ProductAdapter", "Image not found: " + product.getImage());
        }

        // Log product details for debugging
        Log.d("ProductAdapter", "Loaded product: " +
                "\nName: " + product.getName() +
                "\nDescription: " + product.getDetails() +
                "\nPrice: $" + product.getPrice() +
                "\nImage: " + product.getImage());
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("size", "Size: " + product.getSize());
            intent.putExtra("brand", "Brand: " + product.getBrand());
            intent.putExtra("description", product.getDetails());
            intent.putExtra("price", "Price: $" + product.getPrice());
            intent.putExtra("imageResId", imageResource);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("ProductAdapter", "Product List Size get count: " + productList.size());
        return productList.size();
        //return filteredList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice, productSize, productBrand;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }


    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);  // Show all products when no query
        } else {
            String lowerCaseQuery = query.toLowerCase();  // Improve performance by converting query once
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(product);  // Add matching products to the filtered list
                }
            }
        }
        notifyDataSetChanged();  // Update the RecyclerView
    }


    public void updateProductList(List<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);  // Update the complete product list
        filteredList.clear();
        filteredList.addAll(newProductList);  // Reset filtered list to include all products
        notifyDataSetChanged();
    }

}