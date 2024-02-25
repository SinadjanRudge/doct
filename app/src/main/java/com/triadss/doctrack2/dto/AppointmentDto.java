package com.triadss.doctrack2.dto;

import com.google.type.Date;
import com.google.type.DateTime;

import java.sql.Time;

public class AppointmentDto {
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

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public Time getTimeOfAppointment() {
        return timeOfAppointment;
    }

    public void setTimeOfAppointment(Time timeOfAppointment) {
        this.timeOfAppointment = timeOfAppointment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String nameOfRequester;
    private String purpose;
    private Date dateOfAppointment;
    private Time timeOfAppointment;
    private String status;

    
}
