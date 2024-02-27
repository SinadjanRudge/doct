package com.triadss.doctrack2.dto;

public class MedicalHistoryDto {
    private String pastIllness;

    private String prevOperation;

    private String obGyneHist;

    private String familyHist;

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

    public String getObGyneHist() {
        return obGyneHist;
    }

    public void setObGyneHist(String obGyneHist) {
        this.obGyneHist = obGyneHist;
    }

    public String getFamilyHist() {
        return familyHist;
    }

    public void setFamilyHist(String familyHist) {
        this.familyHist = familyHist;
    }
}
