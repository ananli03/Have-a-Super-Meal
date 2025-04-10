package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Entity.Product;
import com.example.myapplication.R;

import java.util.List;
public class ProductPagerAdapter1 extends RecyclerView.Adapter<ProductPagerAdapter1.ProductPageViewHolder> {
    private List<Product> productList;

    public ProductPagerAdapter1(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product1, parent, false);
        return new ProductPageViewHolder(view, this); // 传递适配器引用
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageViewHolder holder, int position) {
        int startIndex = position * 4;
        for (int i = 0; i < 4; i++) {
            if (startIndex + i < productList.size()) {
                Product product = productList.get(startIndex + i);
                holder.bindProduct(i, product, startIndex + i); // 传递适配器位置
            } else {
                holder.hideProduct(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) productList.size() / 4); // 每页4个商品
    }

    static class ProductPageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout[] productLayouts;
        private ImageView[] productImages;
        private TextView[] productNames;
        private TextView[] productAddresses;
        private ImageButton[] productCheckedButtons;
        private ProductPagerAdapter1 adapter; // 添加适配器引用

        public ProductPageViewHolder(@NonNull View itemView, ProductPagerAdapter1 adapter) {
            super(itemView);
            this.adapter = adapter; // 初始化适配器引用
            productLayouts = new RelativeLayout[4];
            productImages = new ImageView[4];
            productNames = new TextView[4];
            productAddresses = new TextView[4];
            productCheckedButtons = new ImageButton[4];

            // 初始化视图组件
            productLayouts[0] = itemView.findViewById(R.id.relativeLayoutProduct0);
            productLayouts[1] = itemView.findViewById(R.id.relativeLayoutProduct1);
            productLayouts[2] = itemView.findViewById(R.id.relativeLayoutProduct2);
            productLayouts[3] = itemView.findViewById(R.id.relativeLayoutProduct3);

            productImages[0] = itemView.findViewById(R.id.imageViewProduct0);
            productImages[1] = itemView.findViewById(R.id.imageViewProduct1);
            productImages[2] = itemView.findViewById(R.id.imageViewProduct2);
            productImages[3] = itemView.findViewById(R.id.imageViewProduct3);

            productNames[0] = itemView.findViewById(R.id.textViewProductName0);
            productNames[1] = itemView.findViewById(R.id.textViewProductName1);
            productNames[2] = itemView.findViewById(R.id.textViewProductName2);
            productNames[3] = itemView.findViewById(R.id.textViewProductName3);

            productAddresses[0] = itemView.findViewById(R.id.textViewProductAddress0);
            productAddresses[1] = itemView.findViewById(R.id.textViewProductAddress1);
            productAddresses[2] = itemView.findViewById(R.id.textViewProductAddress2);
            productAddresses[3] = itemView.findViewById(R.id.textViewProductAddress3);

            // 初始化 ImageButton
            productCheckedButtons[0] = itemView.findViewById(R.id.product_checked0);
            productCheckedButtons[1] = itemView.findViewById(R.id.product_checked1);
            productCheckedButtons[2] = itemView.findViewById(R.id.product_checked2);
            productCheckedButtons[3] = itemView.findViewById(R.id.product_checked3);
        }

        public void bindProduct(int index, Product product, int adapterPosition) {
            productLayouts[index].setVisibility(View.VISIBLE);
            productImages[index].setImageResource(product.getImageResId());
            productNames[index].setText(product.getName());
            productAddresses[index].setText(product.getAddress());

            // 设置按钮的状态
            productCheckedButtons[index].setImageResource(product.isChecked() ? R.drawable.checked : R.drawable.unchecked);

            // 设置按钮点击事件
            productCheckedButtons[index].setOnClickListener(v -> {
                // 切换选中状态
                product.setChecked(!product.isChecked());
                // 更新按钮图标
                productCheckedButtons[index].setImageResource(product.isChecked() ? R.drawable.checked : R.drawable.unchecked);

                // 更新数据库中的 checked 属性
                DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                dbHelper.updateProductCheckedStatus(product.getName(), product.isChecked());

                // 刷新视图
                adapter.updateProduct(adapterPosition); // 使用传入的适配器引用
            });
        }

        public void hideProduct(int index) {
            productLayouts[index].setVisibility(View.GONE);
        }
    }

    public void updateProduct(int position) {
        notifyItemChanged(position);
    }
}
