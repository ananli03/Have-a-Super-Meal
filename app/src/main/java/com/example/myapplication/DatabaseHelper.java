package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Entity.Product;
import com.example.myapplication.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2; // 更新版本号

    // 表名
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PRODUCTS = "products"; // 新的 products 表

    // 用户表的列名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_LOCATION = "location";

    // 产品表的列名
    public static final String COLUMN_PRODUCT_ID = "product_id"; // 产品 ID
    public static final String COLUMN_IMAGE_RES_ID = "imageResId"; // 产品图片资源 ID
    public static final String COLUMN_NAME = "name"; // 产品名称
    public static final String COLUMN_ADDRESS = "address"; // 产品地址
    public static final String COLUMN_CHECKED = "checked"; // 是否选中
    public static final String COLUMN_CLASSIFY = "classify"; // 分类

    // 创建用户表的 SQL 语句
    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_LOCATION + " TEXT);";

    // 创建产品表的 SQL 语句
    private static final String TABLE_PRODUCTS_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_RES_ID + " INTEGER, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_CHECKED + " INTEGER, " +
                    COLUMN_CLASSIFY + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_CREATE); // 创建用户表
        db.execSQL(TABLE_PRODUCTS_CREATE); // 创建产品表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS); // 如果用户表存在，删除它
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS); // 如果产品表存在，删除它
        onCreate(db); // 重新创建表
    }

    // 根据用户名和密码查询用户
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close(); // 关闭 Cursor
        return exists; // 返回用户是否存在
    }

    public void addUser(String username, String password, int age, String gender, String location) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues(); // 创建一个 ContentValues 对象
        values.put(COLUMN_USERNAME, username); // 添加用户名
        values.put(COLUMN_PASSWORD, password); // 添加密码
        values.put(COLUMN_AGE, age); // 添加年龄
        values.put(COLUMN_GENDER, gender); // 添加性别
        values.put(COLUMN_LOCATION, location); // 添加位置

        try {
            // 插入数据并获取生成的行 ID
            long newRowId = db.insert(TABLE_USERS, null, values);
            if (newRowId == -1) {
                // 插入失败
                throw new RuntimeException("Error while inserting user data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // 关闭数据库连接
        }
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;
        try {
            cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                user = new User(username, password, age, gender, location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // 确保游标关闭
            }
            db.close(); // 关闭数据库连接
        }
        return user; // 返回用户信息
    }

    public boolean updateUserProfile(String username, String age, String gender, String location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_LOCATION, location);

        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        return rowsUpdated > 0; // 如果更新了行数大于0，则表示成功
    }

    // 添加产品
    public void addProduct(Integer ImageResId,String Name,String Address,Boolean Checked,String Classify) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_RES_ID,ImageResId);
        values.put(COLUMN_NAME, Name);
        values.put(COLUMN_ADDRESS, Address);
        values.put(COLUMN_CHECKED, Checked ? 1 : 0);
        values.put(COLUMN_CLASSIFY, Classify);

        try {
            long newRowId = db.insert(TABLE_PRODUCTS, null, values);
            if (newRowId == -1) {
                throw new RuntimeException("Error while inserting product data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 使用正确的列名
        Cursor cursor = db.query("products", new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"}, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId")); // 确保使用正确的列名
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECKED)) == 1;
                // 其他字段...

                // 创建产品对象并添加到列表
                Product product = new Product( name, address, imageResId,checked);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }
    public List<Product> getCheckedProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 查询 checked 为 true 的产品
        Cursor cursor = db.query("products",
                new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"},
                "checked = ?", // WHERE 子句
                new String[]{"1"}, // 参数
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;

                // 创建产品对象并添加到列表
                Product product = new Product(name, address, imageResId, checked);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }

    public void clearProductsTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // 使用 DELETE 语句清空 products 表
            db.execSQL("DELETE FROM " + TABLE_PRODUCTS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // 关闭数据库连接
        }
    }
    public boolean updateProductCheckedStatus(String name, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHECKED, checked ? 1 : 0); // 将 boolean 转换为 int

        int rowsUpdated = db.update(TABLE_PRODUCTS, values, COLUMN_NAME + " = ?", new String[]{name});
        return rowsUpdated > 0; // 返回是否更新成功
    }

    // 获取 classify 为 "take_out" 或 "all" 的商品
    public List<Product> getTakeOutProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 使用 OR 条件来获取 classify 为 "take_out" 或 "all" 的商品
        String selection = "classify = ? OR classify = ?";
        String[] selectionArgs = new String[]{"take_out", "all"};

        Cursor cursor = db.query("products",
                new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"},
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;

                // 创建产品对象并添加到列表
                Product product = new Product(name, address, imageResId, checked);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }
    public List<Product> searchTakeOutProducts(String query) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 使用 AND 和 OR 条件来获取 classify 为 "take_out" 或 "all" 且 name 模糊匹配的商品
        String selection = "(classify = ? OR classify = ?) AND name LIKE ?";
        String[] selectionArgs = new String[]{"take_out", "all", "%" + query + "%"};

        Cursor cursor = db.query("products",
                new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"},
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;

                // 创建产品对象并添加到列表
                Product product = new Product(name, address, imageResId, checked);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }


    // 获取 classify 为 "eat_in" 或 "all" 的商品
    public List<Product> getEatInProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 使用 OR 条件来获取 classify 为 "eat_in" 或 "all" 的商品
        String selection = "classify = ? OR classify = ?";
        String[] selectionArgs = new String[]{"eat_in", "all"};

        Cursor cursor = db.query("products",
                new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"},
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;

                // 创建产品对象并添加到列表
                Product product = new Product(name, address, imageResId, checked);
                productList.add(product);
            }
            cursor.close();
        }

        return productList;
    }
    public List<String> getDistinctAddresses() {
        List<String> addresses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "classify = ? OR classify = ?";
        String[] selectionArgs = new String[]{"eat_in", "all"};
        Cursor cursor = db.query("products", new String[]{"DISTINCT address"}, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                addresses.add(cursor.getString(0));
            }
            cursor.close();
        }
        return addresses;
    }
    public List<Product> getEatInProductsByAddress(String address) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 修改 selection 字符串，使用括号明确逻辑关系
        String selection = "(classify = ? OR classify = ?) AND address = ?";
        String[] selectionArgs = new String[]{"eat_in", "all", address};

        Cursor cursor = db.query("products", new String[]{"product_id", "imageResId", "name", "address", "checked", "classify"}, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address1 = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;
                Product product = new Product(name, address1, imageResId, checked);
                productList.add(product);
            }
            cursor.close();
        }
        return productList;
    }





}
