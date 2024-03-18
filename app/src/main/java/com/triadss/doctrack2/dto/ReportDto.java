package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class ReportDto {
    public ReportDto()
    {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    private String message;
    private Timestamp createdDate;
    private String action;
}
