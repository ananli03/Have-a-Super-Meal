package com.example.myapplication.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Adapter.EatInProductPagerAdapter;
import com.example.myapplication.Adapter.TakeOutProductPagerAdapter;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Entity.Product;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class EatInActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<Product> eatInProductList;
    private ViewPager2 viewPager;
    private EatInProductPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_in); // 替换为你的布局文件名

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回按钮
        getSupportActionBar().setHomeButtonEnabled(true); // 启用返回按钮

        // 清除标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(this); // 使用 this 而不是 getContext()

        // 设置 ViewPager2
        viewPager = findViewById(R.id.viewPager4); // 使用 findViewById 而不是 view.findViewById
        adapter = new EatInProductPagerAdapter(new ArrayList<>());
        viewPager.setAdapter(adapter);

        // 设置 TabLayout
        tabLayout = findViewById(R.id.eatInTabLayout);
        loadTabs();

        // 设置垂直滑动
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setPageTransformer(new EatInActivity.VerticalPageTransformer()); // 使用 this 来调用 VerticalPageTransformer

        adapter.updateData(dbHelper.getEatInProducts());
        viewPager.setAdapter(adapter);
    }

    private void loadTabs() {
        List<String> addresses = dbHelper.getDistinctAddresses();
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        for (String address : addresses) {
            tabLayout.addTab(tabLayout.newTab().setText(address));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedAddress = tab.getText().toString();
                if(selectedAddress=="全部"){
                    eatInProductList = dbHelper.getEatInProducts();
                }
                else {
                    eatInProductList = dbHelper.getEatInProductsByAddress(selectedAddress);
                }

                adapter.updateData(eatInProductList);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }





    public class VerticalPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position < -1) { // [-Infinity,-1)]
                page.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                page.setAlpha(1 - Math.abs(position));
                page.setTranslationY(-position * page.getHeight());
            } else { // (1,+Infinity]
                page.setAlpha(0);
            }
        }
    }
}
