package com.student.fahrtenbuchapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;



public class Drive extends RealmObject{

    @SerializedName("user")
    private String user;
    @SerializedName("car")
    private String car;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("startCoord")
    private LocationStart startCoord;
    @SerializedName("endCoord")
    private LocationStop endCoord;
    @SerializedName("startAddress")
    private AddressStart startAddress;
    @SerializedName("endAddress")
    private AddressStop endAddress;
    @SerializedName("startPOI")
    private String startPOI;
    @SerializedName("endPOI")
    private String endPOI;
    @SerializedName("startMileage")
    private Integer startMileage;
    @SerializedName("endMileage")
    private Integer endMileage;
    @SerializedName("usedkwh")
    private Double usedkWh;


    public Drive(){}

    public Drive(String user, String car, String startDate, String endDate, LocationStart startCoord, LocationStop endCoord, AddressStart startAddress, AddressStop endAddress, String startPOI, String endPOI, Integer startMileage, Integer endMileage, Double usedkWh) {
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.startPOI = startPOI;
        this.endPOI = endPOI;
        this.startMileage = startMileage;
        this.endMileage = endMileage;
        this.usedkWh = usedkWh;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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
