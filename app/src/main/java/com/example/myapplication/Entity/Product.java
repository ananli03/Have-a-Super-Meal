package com.example.myapplication.Entity;

public class Product {
    private int imageResId;
    private String name;
    private String address;
    private boolean checked;
    private String classify;

    public Product(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public Product(String name, String address, int imageResId) {
        this.imageResId = imageResId;
        this.name = name;
        this.address = address;
    }
    public Product(String name, String address, int imageResId,boolean checked) {
        this.imageResId = imageResId;
        this.name = name;
        this.address = address;
        this.checked=checked;
    }

    public Product(String name, String address, int imageResId, boolean checked, String classify) {
        this.imageResId = imageResId;
        this.name = name;
        this.address = address;
        this.checked = checked;
        this.classify = classify;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getClassify() {
        return classify;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }
}
