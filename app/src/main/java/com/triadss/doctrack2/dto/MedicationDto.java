package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

public class MedicationDto {
    public MedicationDto()
    {
        // required for Firestore deserialization
    }


    public MedicationDto(String mediId, String patientId, String medicine, String note, Timestamp timestamp)
    {
        this.mediId = mediId;
        this.patientId = patientId;
        this.medicine = medicine;
        this.note = note;
        this.timestamp = timestamp;
    }

    public String getMediId() {
        return mediId;
    }

    public void setMediId(String mediId) {
        this.mediId = mediId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
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

    private String mediId;
    private String patientId;
    private String medicine;
    private String note;
    private Timestamp timestamp;
}