package com.student.fahrtenbuchapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;



public class Drive extends RealmObject{

    private String user;
    private String car;
    private Date startDate;
    private Date endDate;
    private LocationStart startCoord;
    private LocationStop endCoord;
    private AddressStart startAddress;
    private AddressStop endAddress;
    private String startPOI;
    private String endPOI;
    private Integer startMileage;
    private Integer endMileage;
    private Double usedkWh;


    public String getUser() {
        return user;
    }

    public void setUser(String userId) {
        this.user = userId;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String carId) {
        this.car = carId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public LocationStart getStartCoord() {
        return startCoord;
    }

    public void setStartCoord(LocationStart startCoord) {
        this.startCoord = startCoord;
    }

    public LocationStop getEndCoord() {
        return endCoord;
    }

    public void setEndCoord(LocationStop endCoord) {
        this.endCoord = endCoord;
    }

    public AddressStart getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(AddressStart startAddress) {
        this.startAddress = startAddress;
    }

    public AddressStop getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(AddressStop endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartPOI() {
        return startPOI;
    }

    public void setStartPOI(String startPOI) {
        this.startPOI = startPOI;
    }

    public String getEndPOI() {
        return endPOI;
    }

    public void setEndPOI(String endPOI) {
        this.endPOI = endPOI;
    }

    public Integer getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Integer startMileage) {
        this.startMileage = startMileage;
    }

    public Integer getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Integer endMileage) {
        this.endMileage = endMileage;
    }

    public Double getUsedkWh() {
        return usedkWh;
    }

    public void setUsedkWh(Double usedkWh) {
        this.usedkWh = usedkWh;
    }
}
