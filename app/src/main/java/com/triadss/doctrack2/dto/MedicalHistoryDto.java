package com.triadss.doctrack2.dto;

import com.google.type.DateTime;

public class MedicalHistoryDto {
    public int getMedHistId() {
        return medHistId;
    }

    public void setMedHistId(int medHistId) {
        this.medHistId = medHistId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPastIllness() {
        return pastIllness;
    }

    public void setPastIllness(String pastIllness) {
        this.pastIllness = pastIllness;
    }

    public String getPrevOperation() {
        return prevOperation;
    }

    public void setPrevOperation(String prevOperation) {
        this.prevOperation = prevOperation;
    }

    public String getObgyneHist() {
        return obgyneHist;
    }

    public void setObgyneHist(String obgyneHist) {
        this.obgyneHist = obgyneHist;
    }

    public String getFamilyHist() {
        return familyHist;
    }

    public void setFamilyHist(String familyHist) {
        this.familyHist = familyHist;
    }

    private int medHistId;
    private int patientId;
    private String pastIllness;
    private String prevOperation;
    private String obgyneHist;
    private String familyHist;
}
