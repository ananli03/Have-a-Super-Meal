package com.example.myapplication.Fragment;
// HotProductsFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Adapter.ProductPagerAdapter1;
import com.example.myapplication.Adapter.ProductPagerAdapter2;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Entity.Product;
import com.example.myapplication.R;

import java.util.List;

public class RecentSelectionsFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_selections, container, false);

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(getContext());
        productList = dbHelper.getAllProducts(); // 从数据库中获取所有产品信息

        // 设置 ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewPager2);
        ProductPagerAdapter2 adapter = new ProductPagerAdapter2(productList);
        viewPager.setAdapter(adapter);

        // 设置垂直滑动
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager.setPageTransformer(new VerticalPageTransformer());

        return view;
    }
    public class VerticalPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position < -1) { // [-Infinity,-1)
                page.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                // 设置透明度
                page.setAlpha(1 - Math.abs(position));
                // 设置Y轴平移
                page.setTranslationY(-position * page.getHeight());
            } else { // (1,+Infinity]
                page.setAlpha(0);
            }
        }
    }




}
