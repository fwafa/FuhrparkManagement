package com.student.fahrtenbuchapp.realm.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Fabian on 07.12.16.
 */

public class Drive extends RealmObject {

    private User driver;
    private Date startDate;
    private Date endDate;
    private Location startCoord;
    private Location endCoord;
    private Address startAddress;
    private Address endAddress;
    private String startPOI;
    private String endPOI;
    private Integer startMilage;
    private Integer endMileage;
    private Double usedkWh;

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
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

    public Location getStartCoord() {
        return startCoord;
    }

    public void setStartCoord(Location startCoord) {
        this.startCoord = startCoord;
    }

    public Location getEndCoord() {
        return endCoord;
    }

    public void setEndCoord(Location endCoord) {
        this.endCoord = endCoord;
    }

    public Address getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(Address startAddress) {
        this.startAddress = startAddress;
    }

    public Address getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(Address endAddress) {
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

    public Integer getStartMilage() {
        return startMilage;
    }

    public void setStartMilage(Integer startMilage) {
        this.startMilage = startMilage;
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
