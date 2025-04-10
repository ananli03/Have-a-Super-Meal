package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // 导入 Toolbar
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragment.AddFragment;
import com.example.myapplication.Fragment.ClassifyFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.ProfileFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar; // 声明 Toolbar 变量
    private Toolbar customToolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // 设置布局

        // 初始化 Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 清除标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 设置底部导航栏的点击事件监听器
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 根据点击的菜单项切换显示的 Fragment
                if (item.getItemId() == R.id.nav_home) {
                    // 切换到首页 Fragment
                    loadFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.nav_search) {
                    // 切换到搜索 Fragment
                    loadFragment(new ClassifyFragment());
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    // 切换到个人信息 Fragment
                    // 获取传递的用户名
                    String username = getIntent().getStringExtra("username");
                    ProfileFragment profileFragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    args.putString("username", username);
                    profileFragment.setArguments(args);
                    loadFragment(profileFragment);

                    return true;
                } else if (item.getItemId() == R.id.nav_add) {
                    // 切换到搜索 Fragment
                    loadFragment(new AddFragment());
                    return true;
                }  else {
                    return false;
                }
            }
        });

        // 默认加载首页 Fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

    }

    public void setToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
    private void loadFragment(Fragment fragment) {
        // 加载 Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 检查当前显示的 Fragment 是否是首页 Fragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (currentFragment instanceof HomeFragment) {
                // 如果是首页 Fragment，调用 finish() 退出程序
                showExitConfirmationDialog();
                return true;
            } else {
                // 如果不是首页 Fragment，返回到首页 Fragment
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
        }


    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认退出")
                .setMessage("您确定要退出应用吗？")
                .setPositiveButton("是", (dialog, which) -> {
                    finish(); // 关闭当前活动
                })
                .setNegativeButton("否", (dialog, which) -> {
                    dialog.dismiss(); // 关闭对话框
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
