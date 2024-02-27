package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

public class MedicationDto {

    public MedicationDto(int mediId, int patientId, String medicine, String note, Timestamp timestamp)
    {
        this.mediId = mediId;
        this.patientId = patientId;
        this.medicine = medicine;
        this.note = note;
        this.timestamp = timestamp;
    }


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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    private int mediId;
    private int patientId;
    private String medicine;
    private String note;
    private Timestamp timestamp;
}
