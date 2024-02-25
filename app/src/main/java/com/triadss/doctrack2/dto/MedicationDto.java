package com.triadss.doctrack2.dto;

import com.google.type.DateTime;

import java.sql.Time;
import java.util.Date;

public class MedicationDto {
    public int getMediID() {
        return mediID;
    }

    public void setMediID(int mediID) {
        this.mediID = mediID;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getMedDate() {
        return medDate;
    }

    public void setMedDate(Date medDate) {
        this.medDate = medDate;
    }

    public Time getMedTime() {
        return medTime;
    }

    public void setMedTime(Time medTime) {
        this.medTime = medTime;
    }

    private int mediID;
    private int patientId;
    private String medicine;
    private String note;
    private Date medDate;
    private Time medTime;
}
