package com.example.myapplication.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
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



import java.util.ArrayList;
import java.util.List;

public class ProductPagerAdapter2 extends RecyclerView.Adapter<ProductPagerAdapter2.ProductPageViewHolder> {
    private List<Product> productList; // 原始商品列表
    private List<Product> checkedProductList; // 只包含 checked=true 的商品列表

    public ProductPagerAdapter2(List<Product> productList) {
        this.productList = productList;
        this.checkedProductList = new ArrayList<>();
        filterCheckedProducts();
    }

    private void filterCheckedProducts() {
        checkedProductList.clear();
        for (Product product : productList) {
            if (product.isChecked()) {
                checkedProductList.add(product);
            }
        }
    }

    @NonNull
    @Override
    public ProductPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_checked, parent, false);
        return new ProductPageViewHolder(view, this); // 传递适配器引用
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageViewHolder holder, int position) {
        int startIndex = position * 4;
        for (int i = 0; i < 4; i++) {
            if (startIndex + i < checkedProductList.size()) {
                Product product = checkedProductList.get(startIndex + i);
                holder.bindProduct(i, product, startIndex + i); // 传递适配器位置
            } else {
                holder.hideProduct(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) checkedProductList.size() / 4); // 每页4个商品
    }

    static class ProductPageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout[] productLayouts;
        private ImageView[] productImages;
        private TextView[] productNames;
        private TextView[] productAddresses;
        private ImageButton[] productCheckedButtons;
        private ProductPagerAdapter2 adapter; // 添加适配器引用
        private ImageButton choiceButton; // 添加选择按钮
        private ImageView diceImageView; // 添加骰子图标的 ImageView
        private ObjectAnimator rotationAnimator; // 将动画器作为成员变量

        public ProductPageViewHolder(@NonNull View itemView, ProductPagerAdapter2 adapter) {
            super(itemView);
            this.adapter = adapter; // 初始化适配器引用
            productLayouts = new RelativeLayout[4];
            productImages = new ImageView[4];
            productNames = new TextView[4];
            productAddresses = new TextView[4];
            productCheckedButtons = new ImageButton[4];

            // 初始化视图组件
            productLayouts[0] = itemView.findViewById(R.id.checkedProduct0);
            productLayouts[1] = itemView.findViewById(R.id.checkedProduct1);
            productLayouts[2] = itemView.findViewById(R.id.checkedProduct2);
            productLayouts[3] = itemView.findViewById(R.id.checkedProduct3);

            productImages[0] = itemView.findViewById(R.id.checkedImageViewProduct0);
            productImages[1] = itemView.findViewById(R.id.checkedImageViewProduct1);
            productImages[2] = itemView.findViewById(R.id.checkedImageViewProduct2);
            productImages[3] = itemView.findViewById(R.id.checkedImageViewProduct3);

            productNames[0] = itemView.findViewById(R.id.checkedTextViewProductName0);
            productNames[1] = itemView.findViewById(R.id.checkedTextViewProductName1);
            productNames[2] = itemView.findViewById(R.id.checkedTextViewProductName2);
            productNames[3] = itemView.findViewById(R.id.checkedTextViewProductName3);

            productAddresses[0] = itemView.findViewById(R.id.checkedTextViewProductAddress0);
            productAddresses[1] = itemView.findViewById(R.id.checkedTextViewProductAddress1);
            productAddresses[2] = itemView.findViewById(R.id.checkedTextViewProductAddress2);
            productAddresses[3] = itemView.findViewById(R.id.checkedTextViewProductAddress3);

            // 初始化 ImageButton
            productCheckedButtons[0] = itemView.findViewById(R.id.checkedProduct_checked0);
            productCheckedButtons[1] = itemView.findViewById(R.id.checkedProduct_checked1);
            productCheckedButtons[2] = itemView.findViewById(R.id.checkedProduct_checked2);
            productCheckedButtons[3] = itemView.findViewById(R.id.checkedProduct_checked3);

            // 初始化骰子图标的 ImageView
            diceImageView = itemView.findViewById(R.id.diceImageView); // 确保 ID 与布局中的一致
            // 初始化选择按钮
            choiceButton = itemView.findViewById(R.id.choice);
            choiceButton.setOnClickListener(v -> {
                selectRandomProduct(v.getContext());
            });
        }

        public void bindProduct(int index, Product product, int adapterPosition) {
            productLayouts[index].setVisibility(View.VISIBLE);
            productImages[index].setImageResource(product.getImageResId());
            productNames[index].setText(product.getName());
            productAddresses[index].setText(product.getAddress());



            // 设置按钮点击事件
            productCheckedButtons[index].setOnClickListener(v -> {
                // 切换选中状态
                product.setChecked(false); // 设置为不选中
                // 更新数据库中的 checked 属性
                DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                dbHelper.updateProductCheckedStatus(product.getName(), false);

                // 移除商品
                adapter.checkedProductList.remove(adapterPosition); // 从适配器列表中移除商品
                adapter.notifyDataSetChanged(); // 刷新适配器
            });
        }

        public void hideProduct(int index) {
            productLayouts[index].setVisibility(View.GONE);
        }
        private void selectRandomProduct(Context context) {
            if (adapter.checkedProductList.isEmpty()) {
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("没有可选的商品！")
                        .setPositiveButton("确定", null)
                        .show();
                return;
            }

            // 显示骰子图标
            diceImageView.setVisibility(View.VISIBLE);

            // 如果之前有动画在运行，先取消它
            if (rotationAnimator != null && rotationAnimator.isRunning()) {
                rotationAnimator.cancel();
            }

            // 创建并启动旋转动画
            rotationAnimator = ObjectAnimator.ofFloat(diceImageView, "rotation", 0f, 360f);
            rotationAnimator.setDuration(3000); // 动画持续时间
            rotationAnimator.setRepeatCount(0); // 不重复
            rotationAnimator.start();

            // 随机选择一个产品
            int randomIndex = (int) (Math.random() * adapter.checkedProductList.size());
            Product selectedProduct = adapter.checkedProductList.get(randomIndex);

            // 创建自定义布局
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_product_details, null);
            ImageView dialogProductImage = dialogView.findViewById(R.id.dialogProductImage);
            TextView dialogProductName = dialogView.findViewById(R.id.dialogProductName);
            TextView dialogProductAddress = dialogView.findViewById(R.id.dialogProductAddress);
            ImageButton imageButton = dialogView.findViewById(R.id.imageButton);
            ImageButton imageButton2 = dialogView.findViewById(R.id.imageButton2);

            // 设置商品信息
            dialogProductImage.setImageResource(selectedProduct.getImageResId());
            dialogProductName.setText(selectedProduct.getName());
            dialogProductAddress.setText("地址: " + selectedProduct.getAddress());

            // 创建对话框
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create();

            // 使用 Handler 延迟显示对话框
            rotationAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束后显示对话框
                    dialog.show();

                    // 隐藏骰子图标
                    diceImageView.setVisibility(View.GONE);
                }
            });

            // 设置 imageButton 点击事件，重新启动选择
            imageButton.setOnClickListener(v -> {
                dialog.dismiss();
                // 重新启动骰子旋转动画
                selectRandomProduct(v.getContext());
            });

            // 设置 imageButton2 点击事件，关闭对话框
            imageButton2.setOnClickListener(v -> {
                dialog.dismiss();
            });

            // 确保在对话框关闭时停止动画并隐藏骰子图标
            dialog.setOnDismissListener(dialogInterface -> {
                if (rotationAnimator != null && rotationAnimator.isRunning()) {
                    rotationAnimator.cancel();
                }
                diceImageView.setVisibility(View.GONE);
            });
        }

    }
}