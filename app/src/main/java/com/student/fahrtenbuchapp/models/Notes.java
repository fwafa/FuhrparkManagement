package com.student.fahrtenbuchapp.models;

import io.realm.RealmObject;


public class Notes extends RealmObject {

    private Drive drive;
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}
