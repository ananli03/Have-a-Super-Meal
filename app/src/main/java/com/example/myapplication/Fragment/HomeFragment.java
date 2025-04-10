package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Entity.Product;
import com.example.myapplication.Adapter.ProductPagerAdapter;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout; // 类成员变量
    private FrameLayout frameLayoutContent;
    private DatabaseHelper dbHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 设置 ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        List<Product> products = new ArrayList<>();
        products.add(new Product(R.drawable.product1, "东北家常菜"));
        products.add(new Product(R.drawable.product16, "旺芬园"));
        products.add(new Product(R.drawable.product14, "新疆大盘鸡"));
        products.add(new Product(R.drawable.product4, "擂饭"));
        products.add(new Product(R.drawable.product15, "茶泡饭"));
        // 更多商品数据...

        // 创建适配器并设置
        ProductPagerAdapter adapter = new ProductPagerAdapter(products, viewPager);
        viewPager.setAdapter(adapter);

        // 设置左右滑动时更新焦点商品
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                adapter.setCurrentIndex(position); // 更新焦点商品索引
            }
        });

        // 设置 TabLayout
        tabLayout = view.findViewById(R.id.tabLayout); // 将局部变量赋值给类成员变量

        // 获取 FrameLayout 用于添加 fragment
        frameLayoutContent = view.findViewById(R.id.frameLayoutContent); // 也可以将 FrameLayout 的引用赋值给类成员变量

        setupTabLayout(); // 现在可以安全地调用这个方法
        // 初始化 DatabaseHelper

        dbHelper = new DatabaseHelper(getContext());
        addNewProduct();
        // 默认加载 HotProductsFragment
        if (savedInstanceState == null) {
            switchFragment(0); // 默认选择第一个 Tab
        }
        return view;
    }

    private void setupTabLayout() {
        // 添加 Tab
        tabLayout.addTab(tabLayout.newTab().setText("热门商品"));
        tabLayout.addTab(tabLayout.newTab().setText("最近选择"));

        // 设置 Tab 选中监听
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 这里可以处理未选中状态
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 这里可以处理重新选中状态
            }
        });

        // 默认选中第一个 Tab
        tabLayout.getTabAt(0).select();
    }

    private void switchFragment(int position) {
        Fragment selectedFragment = null;

        // 根据 Tab 的位置选择对应的 Fragment
        switch (position) {
            case 0:
                selectedFragment = new HotProductsFragment(); // 替换为你的 Fragment
                break;
            case 1:
                selectedFragment = new RecentSelectionsFragment(); // 替换为你的 Fragment
                break;
        }

        // 切换 Fragment
        if (selectedFragment != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutContent, selectedFragment);
            transaction.commit();
        }
    }
    private void addNewProduct(){
        /*dbHelper.clearProductsTable();
        dbHelper.addProduct(R.drawable.product1,"东北家常菜","北门",false,"eat_in");
        dbHelper.addProduct(R.drawable.product2,"新疆肉饼","北门",false,"eat_in");
        dbHelper.addProduct(R.drawable.product3,"重庆小面","三区",false,"all");
        dbHelper.addProduct(R.drawable.product4,"擂饭","九华步步高",false,"take_out");
        dbHelper.addProduct(R.drawable.product5,"匠心卤","七区后街",false,"all");
        dbHelper.addProduct(R.drawable.product6,"大碗先生","九华步步高",false,"all");
        dbHelper.addProduct(R.drawable.product7,"三木小町寿司","未知",false,"take_out");
        dbHelper.addProduct(R.drawable.product8,"新疆大盘鸡","七区后街",false,"eat_in");
        dbHelper.addProduct(R.drawable.product9,"万利隆的包子铺","未知",false,"take_out");
        dbHelper.addProduct(R.drawable.product10,"麻辣牛肉面","南堕",false,"eat_in");
        dbHelper.addProduct(R.drawable.product11,"小锅炖","北门",false,"eat_in");
        dbHelper.addProduct(R.drawable.product12,"我们不一样鸡公煲","北门",false,"all");
        dbHelper.addProduct(R.drawable.product13,"舌尖大师","北门",false,"eat_in");
        dbHelper.addProduct(R.drawable.product14,"大盘鸡","七区后街",false,"eat_in");
        dbHelper.addProduct(R.drawable.product15,"茶泡饭","三区",false,"eat_in");
        dbHelper.addProduct(R.drawable.product16,"旺芬园","南堕",false,"eat_in");
        dbHelper.addProduct(R.drawable.product17,"舒芙蕾","南堕",false,"eat_in");
        dbHelper.addProduct(R.drawable.product19,"火锅鸡","南堕",false,"eat_in");*/

    }
}
