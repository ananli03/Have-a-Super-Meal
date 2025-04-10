package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.Fragment.ProfileFragment;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextTextUsername);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextUsername.setText("username");
        editTextPassword.setText("password");
        btnLogin = findViewById(R.id.btn_login);
        // 初始化 DatabaseHelper
        dbHelper = new DatabaseHelper(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 检查输入是否为空
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 确保 dbHelper 已初始化
        if (dbHelper != null) {
            // 查询数据库验证用户
            if (dbHelper.checkUser(username, password)) {
                // 登录成功
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                // 创建 Intent 启动 HomeActivity，并传递用户名
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("username", username); // 传递用户名
                startActivity(intent); // 启动新的活动
                finish(); // 结束当前活动
            } else {
                // 登录失败
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 如果 dbHelper 为 null
            Toast.makeText(this, "Database helper not initialized", Toast.LENGTH_SHORT).show();
        }
    }
    private void showRegisterDialog() {
        // 创建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register User");

        // 设置对话框布局
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_register, null);
        builder.setView(dialogView);

        // 获取输入框和按钮
        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);
        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        EditText editTextGender = dialogView.findViewById(R.id.editTextGender);
        EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);
        Button btnAddUser = dialogView.findViewById(R.id.btnAddUser);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // 创建对话框
        AlertDialog dialog = builder.create();

        // 设置“添加用户”按钮的点击事件
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String ageStr = editTextAge.getText().toString().trim();
                String gender = editTextGender.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();

                // 检查输入
                if (username.isEmpty() || password.isEmpty() || ageStr.isEmpty() || gender.isEmpty() || location.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 添加用户到数据库
                int age = Integer.parseInt(ageStr);
                dbHelper.addUser(username, password, age, gender, location);
                Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                // 关闭对话框
                dialog.dismiss();
            }
        });

        // 设置“取消”按钮的点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 关闭对话框
            }
        });

        // 显示对话框
        dialog.show();
    }

}
//
