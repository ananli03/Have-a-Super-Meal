package com.example.myapplication.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.example.myapplication.Adapter.TakeOutProductPagerAdapter;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Entity.Product;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TakeOutActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<Product> productList;
    private TakeOutProductPagerAdapter adapter;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out); // 替换为你的布局文件名

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回按钮
        getSupportActionBar().setHomeButtonEnabled(true); // 启用返回按钮

        // 清除标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 初始化数据库助手
        dbHelper = new DatabaseHelper(this);

        // 从数据库中获取分类为 "take_out" 的产品信息
        productList = dbHelper.getTakeOutProducts();

        // 设置 ViewPager2
        ViewPager2 viewPager = findViewById(R.id.viewPager3);
        adapter = new TakeOutProductPagerAdapter(productList);
        viewPager.setAdapter(adapter);

        // 设置垂直滑动
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setPageTransformer(new VerticalPageTransformer());

        // 初始化搜索框
        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            // 如果搜索框为空，返回所有产品
            filteredList = dbHelper.getTakeOutProducts();
        } else {
            // 根据输入的字符串进行模糊查询
            filteredList = dbHelper.searchTakeOutProducts(query);
        }
        // 更新适配器的数据
        adapter.updateData(filteredList);
    }

    public class VerticalPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position < -1) { // [-Infinity,-1)
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
