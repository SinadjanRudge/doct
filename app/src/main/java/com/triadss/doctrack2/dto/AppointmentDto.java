package com.triadss.doctrack2.dto;

import com.google.type.Date;
import com.google.type.DateTime;

import java.sql.Time;

public class AppointmentDto {

    public AppointmentDto(int appointmentId, String nameOfRequester, String purpose,
            String dateOfAppointment, long timeOfAppointment, String status) {
        this.appointmentId = appointmentId;
        this.nameOfRequester = nameOfRequester;
        this.purpose = purpose;
        this.dateOfAppointment = dateOfAppointment;
        this.timeOfAppointment = timeOfAppointment;
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

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(String dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public long getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public void setTimeOfAppointment(long timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    private int appointmentId;
    private String nameOfRequester;
    private String purpose;
    private String dateOfAppointment;
    private long timeOfAppointment;
    private String status;

}
