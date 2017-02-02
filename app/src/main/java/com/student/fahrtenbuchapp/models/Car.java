package com.student.fahrtenbuchapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Car extends RealmObject{

    @PrimaryKey
    @SerializedName("_id")
    private String _id;

    @SerializedName("vendor")
    private String vendor;

    @SerializedName("model")
    private String model;

    @SerializedName("licenseNumber")
    private String licenseNumber;

    @SerializedName("mileage")
    private Integer mileage;

    @SerializedName("battery")
    private Integer battery;

    @SerializedName("charging")
    private Boolean charging;

    @SerializedName("chargedkWh")
    private Double chargedkWh;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Boolean getCharging() {
        return charging;
    }

    public void setCharging(Boolean charging) {
        this.charging = charging;
    }

    public Double getChargedkWh() {
        return chargedkWh;
    }

    public void setChargedkWh(Double chargedkWh) {
        this.chargedkWh = chargedkWh;
    }
}
