package com.triadss.doctrack2.dto;

import java.sql.Time;
import java.util.Date;

public class MedicationDto {
    public int getMediId() {
        return mediId;
    }

    public void setMediId(int mediId) {
        this.mediId = mediId;
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

    private int mediId;
    private int patientId;
    private String medicine;
    private String note;
    private Date medDate;
    private Time medTime;
}
