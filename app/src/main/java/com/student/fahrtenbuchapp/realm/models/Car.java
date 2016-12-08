package com.student.fahrtenbuchapp.realm.models;

import io.realm.RealmObject;

/**
 * Created by Fabian on 07.12.16.
 */

public class Car extends RealmObject {
    private String _id;
    private String vendor;
    private String model;
    private String licenseNumber;
    private Integer mileage;
    private Integer battery;
    private Boolean charging;
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
