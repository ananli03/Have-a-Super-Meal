package com.example.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.Activity.TakeOutActivity; // 导入你的外卖活动
import com.example.myapplication.Activity.EatInActivity; // 导入你的堂食活动

public class ClassifyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用 inflater 加载布局
        View view = inflater.inflate(R.layout.fragment_classify, container, false); // 替换为你的布局文件名

        // 找到按钮
        ImageButton takeOutButton = view.findViewById(R.id.take_out);
        ImageButton eatInButton = view.findViewById(R.id.eat_in);

        // 设置外卖按钮的点击事件
        takeOutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TakeOutActivity.class);
            startActivity(intent);
        });

        // 设置堂食按钮的点击事件
        eatInButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EatInActivity.class);
            startActivity(intent);
        });

        return view; // 返回加载的视图
    }
}
