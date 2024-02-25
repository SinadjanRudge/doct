package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

public class AppointmentDto {
    public AppointmentDto() {
        // required for Firestore deserialization
    }
    public AppointmentDto(String patientId, String nameOfRequester, String purpose,
            Timestamp dateOfAppointment, String status) {
        this.patientId = patientId;
        this.nameOfRequester = nameOfRequester;
        this.purpose = purpose;
        this.dateOfAppointment = dateOfAppointment;
        this.status = status;
    }

    public String getNameOfRequester() {
        return nameOfRequester;
    }

    public void setNameOfRequester(String nameOfRequester) {
        this.nameOfRequester = nameOfRequester;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Timestamp getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Timestamp dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String userId) {
        this.patientId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    private String nameOfRequester;
    private String purpose;
    private Timestamp dateOfAppointment;
    private String status;
    private String patientId;
    private Timestamp createdAt;

}
