package com.example.myapplication.Entity;

public class User {
    private String username;
    private String password;
    private int age;
    private String gender;
    private String location;

    public User(String username, String password, int age, String gender, String location) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

    // Getter 方法
    public String getUsername() { return username; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getLocation() { return location; }
}
