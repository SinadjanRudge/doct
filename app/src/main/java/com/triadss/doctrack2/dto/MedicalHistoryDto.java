package com.triadss.doctrack2.dto;

import android.os.Parcelable;
import android.os.Parcel;

import com.google.type.DateTime;

public class MedicalHistoryDto implements Parcelable {
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

    protected MedicalHistoryDto(Parcel in) {
        // Read fields from Parcel
        pastIllness = in.readString();
        prevOperation = in.readString();
        obgyneHist = in.readString();
        familyHist = in.readString();
    }

    public static final Creator<MedicalHistoryDto> CREATOR = new Creator<MedicalHistoryDto>() {
        @Override
        public MedicalHistoryDto createFromParcel(Parcel in) {
            return new MedicalHistoryDto(in);
        }

        @Override
        public MedicalHistoryDto[] newArray(int size) {
            return new MedicalHistoryDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write fields to Parcel
        dest.writeString(pastIllness);
        dest.writeString(prevOperation);
        dest.writeString(obgyneHist);
        dest.writeString(familyHist);
    }
}
