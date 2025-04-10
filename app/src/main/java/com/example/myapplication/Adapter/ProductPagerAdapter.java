package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Entity.Product;
import com.example.myapplication.R;
import java.util.List;

public class ProductPagerAdapter extends RecyclerView.Adapter<ProductPagerAdapter.ViewHolder> {

    private List<Product> productList;
    private int currentIndex = 0;
    private ViewPager2 viewPager; // 添加 ViewPager2 引用

    public ProductPagerAdapter(List<Product> productList, ViewPager2 viewPager) {
        this.productList = productList;
        this.viewPager = viewPager; // 初始化 ViewPager2
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 计算前后商品的索引
        int prevIndex = position - 1;
        int nextIndex = position + 1;

        // 设置当前商品
        Product currentProduct = productList.get(position);
        holder.productImage.setImageResource(currentProduct.getImageResId());
        holder.productName.setText(currentProduct.getName());

        // 设置前一个商品
        if (prevIndex >= 0) {
            Product prevProduct = productList.get(prevIndex);
            holder.productImage1.setImageResource(prevProduct.getImageResId());
            holder.productImage1.setVisibility(View.VISIBLE);
            holder.productImage1.setOnClickListener(v -> {
                setCurrentIndex(prevIndex);
                viewPager.setCurrentItem(prevIndex, true); // 切换到前一个商品
            });
        } else {
            holder.productImage1.setVisibility(View.INVISIBLE); // 隐藏前一个商品
        }

        // 设置后一个商品
        if (nextIndex < productList.size()) {
            Product nextProduct = productList.get(nextIndex);
            holder.productImage2.setImageResource(nextProduct.getImageResId());
            holder.productImage2.setVisibility(View.VISIBLE);
            holder.productImage2.setOnClickListener(v -> {
                setCurrentIndex(nextIndex);
                viewPager.setCurrentItem(nextIndex, true); // 切换到后一个商品
            });
        } else {
            holder.productImage2.setVisibility(View.INVISIBLE); // 隐藏后一个商品
        }

        // 设置透明度和缩放效果
        setProductVisibility(holder, position);
    }

    private void setProductVisibility(ViewHolder holder, int position) {
        // 当前商品
        if (position == currentIndex) {
            holder.productImage.setAlpha(1f);
            holder.productImage.setScaleX(1f);
            holder.productImage.setScaleY(1f);
        } else {
            holder.productImage.setAlpha(0.5f);
            holder.productImage.setScaleX(0.8f);
            holder.productImage.setScaleY(0.8f);
        }

        // 前一个商品
        if (position == currentIndex - 1) {
            holder.productImage1.setAlpha(1f);
            holder.productImage1.setScaleX(1f);
            holder.productImage1.setScaleY(1f);
        } else {
            holder.productImage1.setAlpha(0.5f);
            holder.productImage1.setScaleX(0.8f);
            holder.productImage1.setScaleY(0.8f);
        }

        // 后一个商品
        if (position == currentIndex + 1) {
            holder.productImage2.setAlpha(1f);
            holder.productImage2.setScaleX(1f);
            holder.productImage2.setScaleY(1f);
        } else {
            holder.productImage2.setAlpha(0.5f);
            holder.productImage2.setScaleX(0.8f);
            holder.productImage2.setScaleY(0.8f);
        }
        if (position == currentIndex) {
            holder.productName.setVisibility(View.VISIBLE);
        } else {
            holder.productName.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setCurrentIndex(int index) {
        if (index >= 0 && index < productList.size()) {
            this.currentIndex = index;
            notifyDataSetChanged();  // 更新商品焦点
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        ImageView productImage1;
        ImageView productImage2;
        TextView productName;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productImage1 = itemView.findViewById(R.id.product_image1);
            productImage2 = itemView.findViewById(R.id.product_image2);
            productName = itemView.findViewById(R.id.product_name);
        }
    }
}
