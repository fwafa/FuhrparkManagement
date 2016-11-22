package com.student.fahrtenbuchapp.models;


import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Model {

    // role implementieren

    private String name;
    private String role;
    private String surname;
    private String username;
    private String password;
    private String date;

    private String id;
    private String vendor;
    private String model;
    private int mileage;
    private int battery;
    private boolean availability;

    private Drawable imageButtonCar;
    private Drawable thumbUp;
    private Drawable thumbDown;
    private Drawable imgBattery;

    private Button resButton;



    public Button getResButton() {
        return resButton;
    }

    public void setResButton(Button resButton) {
        this.resButton = resButton;
    }

    public Drawable getImgBattery() {
        return imgBattery;
    }

    public void setImgBattery(Drawable imgBattery) {
        this.imgBattery = imgBattery;
    }

    public Drawable getThumbUp() {
        return thumbUp;
    }

    public void setThumbUp(Drawable thumbUp) {
        this.thumbUp = thumbUp;
    }

    public Drawable getThumbDown() {
        return thumbDown;
    }

    public void setThumbDown(Drawable thumbDown) {
        this.thumbDown = thumbDown;
    }

    public Drawable getImageButtonCar() {
        return imageButtonCar;
    }

    public void setImageButtonCar(Drawable imageButtonCar) {
        this.imageButtonCar = imageButtonCar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
