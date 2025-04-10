package com.example.myapplication.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.Entity.User;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView;
    private TextView ageTextView;
    private TextView genderTextView;
    private TextView locationTextView;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 初始化 TextView
        usernameTextView = view.findViewById(R.id.usernameTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        genderTextView = view.findViewById(R.id.genderTextView);
        locationTextView = view.findViewById(R.id.locationTextView);

        dbHelper = new DatabaseHelper(getContext());

        // 从 arguments 获取用户名
        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString("username");
            loadUserProfile(username);
        }

        // 点击编辑按钮弹出对话框
        ImageView profileChange = view.findViewById(R.id.profile_change);
        profileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        return view;
    }

    private void loadUserProfile(String username) {
        // 从数据库加载用户信息
        User user = dbHelper.getUserByUsername(username);
        if (user != null) {
            Log.d("ProfileFragment", "Loaded user: " + user.getUsername());
            usernameTextView.setText(user.getUsername());
            ageTextView.setText(user.getAge() + " Age");
            genderTextView.setText(user.getGender());
            locationTextView.setText(user.getLocation());
        } else {
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog() {
        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText ageEditText = dialogView.findViewById(R.id.ageEditText);
        EditText genderEditText = dialogView.findViewById(R.id.genderEditText);
        EditText locationEditText = dialogView.findViewById(R.id.locationEditText);
        Button submitButton = dialogView.findViewById(R.id.change_submit);
        Button cancelButton = dialogView.findViewById(R.id.change_cancel);

        // 获取当前用户名
        String username = usernameTextView.getText().toString();

        // 从数据库获取用户信息并填充到对话框
        User user = dbHelper.getUserByUsername(username);
        if (user != null) {
            // 确保值是字符串类型，如果是数字类型需要转换为字符串
            ageEditText.setText(String.valueOf(user.getAge())); // 将年龄转换为字符串
            genderEditText.setText(user.getGender()); // 性别直接填充
            locationEditText.setText(user.getLocation()); // 地点直接填充
        }

        AlertDialog dialog = builder.create();

        // 提交按钮的点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age = ageEditText.getText().toString();
                String gender = genderEditText.getText().toString();
                String location = locationEditText.getText().toString();

                // 更新数据库
                boolean isUpdated = updateUserInfo(username, age, gender, location);

                if (isUpdated) {
                    // 更新界面上的 TextView
                    ageTextView.setText(age + " Age");
                    genderTextView.setText(gender);
                    locationTextView.setText(location);
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss(); // 关闭对话框
            }
        });

        // 取消按钮的点击事件
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 关闭对话框
            }
        });

        dialog.show(); // 显示对话框
    }


    private boolean updateUserInfo(String username, String age, String gender, String location) {
        // 在这里更新数据库，返回更新是否成功
        return dbHelper.updateUserProfile(username, age, gender, location);
    }
}
