package com.example.myapplication.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
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
public class EatInProductPagerAdapter extends RecyclerView.Adapter<EatInProductPagerAdapter.ProductPageViewHolder> {
    private List<Product> eatInProductList;

    public EatInProductPagerAdapter(List<Product> productList) {
        this.eatInProductList = productList;
    }
    public void updateData(List<Product> productList){
        this.eatInProductList = productList;
    }
    //TakeOutProductPagerAdapter.java
    @NonNull
    @Override
    public ProductPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.take_out_product, parent, false);
        return new ProductPageViewHolder(view, this); // 传递适配器引用
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPageViewHolder holder, int position) {
        int startIndex = position * 7;
        for (int i = 0; i <7; i++) {
            if (startIndex + i < eatInProductList.size()) {
                Product product = eatInProductList.get(startIndex + i);
                holder.bindProduct(i, product, startIndex + i); // 传递适配器位置
            } else {
                holder.hideProduct(i);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil((double) eatInProductList.size() / 7); // 每页4个商品
    }

    static class ProductPageViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout[] productLayouts;
        private ImageView[] productImages;
        private TextView[] productNames;
        private TextView[] productAddresses;
        private ImageButton[] productCheckedButtons;
        private EatInProductPagerAdapter adapter; // 添加适配器引用
        private ImageButton choiceButton; // 添加选择按钮
        private ImageView diceImageView; // 添加骰子图标的 ImageView
        private ObjectAnimator rotationAnimator; // 将动画器作为成员变量

        public ProductPageViewHolder(@NonNull View itemView, EatInProductPagerAdapter adapter) {
            super(itemView);
            this.adapter = adapter; // 初始化适配器引用
            productLayouts = new RelativeLayout[7];
            productImages = new ImageView[7];
            productNames = new TextView[7];
            productAddresses = new TextView[7];
            productCheckedButtons = new ImageButton[7];

            // 初始化视图组件
            productLayouts[0] = itemView.findViewById(R.id.takeOutProduct1);
            productLayouts[1] = itemView.findViewById(R.id.takeOutProduct2);
            productLayouts[2] = itemView.findViewById(R.id.takeOutProduct3);
            productLayouts[3] = itemView.findViewById(R.id.takeOutProduct4);
            productLayouts[4] = itemView.findViewById(R.id.takeOutProduct5);
            productLayouts[5] = itemView.findViewById(R.id.takeOutProduct6);
            productLayouts[6] = itemView.findViewById(R.id.takeOutProduct7);

            productImages[0] = itemView.findViewById(R.id.takeOutImageView1);
            productImages[1] = itemView.findViewById(R.id.takeOutImageView2);
            productImages[2] = itemView.findViewById(R.id.takeOutImageView3);
            productImages[3] = itemView.findViewById(R.id.takeOutImageView4);
            productImages[4] = itemView.findViewById(R.id.takeOutImageView5);
            productImages[5] = itemView.findViewById(R.id.takeOutImageView6);
            productImages[6] = itemView.findViewById(R.id.takeOutImageView7);



            productNames[0] = itemView.findViewById(R.id.takeOutProductName1);
            productNames[1] = itemView.findViewById(R.id.takeOutProductName2);
            productNames[2] = itemView.findViewById(R.id.takeOutProductName3);
            productNames[3] = itemView.findViewById(R.id.takeOutProductName4);
            productNames[4] = itemView.findViewById(R.id.takeOutProductName5);
            productNames[5] = itemView.findViewById(R.id.takeOutProductName6);
            productNames[6] = itemView.findViewById(R.id.takeOutProductName7);



            productAddresses[0] = itemView.findViewById(R.id.takeOutProductAddress1);
            productAddresses[1] = itemView.findViewById(R.id.takeOutProductAddress2);
            productAddresses[2] = itemView.findViewById(R.id.takeOutProductAddress3);
            productAddresses[3] = itemView.findViewById(R.id.takeOutProductAddress4);
            productAddresses[4] = itemView.findViewById(R.id.takeOutProductAddress5);
            productAddresses[5] = itemView.findViewById(R.id.takeOutProductAddress6);
            productAddresses[6] = itemView.findViewById(R.id.takeOutProductAddress7);

            // 初始化 ImageButton

            productCheckedButtons[0] = itemView.findViewById(R.id.takeOut_checked1);
            productCheckedButtons[1] = itemView.findViewById(R.id.takeOut_checked2);
            productCheckedButtons[2] = itemView.findViewById(R.id.takeOut_checked3);
            productCheckedButtons[3] = itemView.findViewById(R.id.takeOut_checked4);
            productCheckedButtons[4] = itemView.findViewById(R.id.takeOut_checked5);
            productCheckedButtons[5] = itemView.findViewById(R.id.takeOut_checked6);
            productCheckedButtons[6] = itemView.findViewById(R.id.takeOut_checked7);

            // 初始化骰子图标的 ImageView
            diceImageView = itemView.findViewById(R.id.takeOutDiceImageView); // 确保 ID 与布局中的一致
            // 初始化选择按钮
            choiceButton = itemView.findViewById(R.id.takeOutChoice);
            choiceButton.setOnClickListener(v -> {
                selectRandomProduct(v.getContext());
            });
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
        private void selectRandomProduct(Context context) {
            if (adapter.eatInProductList.isEmpty()) {
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
            int randomIndex = (int) (Math.random() * adapter.eatInProductList.size());
            Product selectedProduct = adapter.eatInProductList.get(randomIndex);

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

    public void updateProduct(int position) {
        notifyItemChanged(position);
    }

}
