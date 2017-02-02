package com.student.fahrtenbuchapp.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PointOfInterests extends RealmObject {

    @SerializedName("poi")
    private String poi;

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }
}
