package com.triadss.doctrack2.config.model;

/**
 * This class represents the USER model in FireStore.
 * It contains constants for the property names to avoid misspelling when interacting with FireStore.
 */
public class MedicationModel extends AuditModel {
    public static final String mediID = "mediID";
    public static final String patientID = "patientID";
    public static final String medicine = "medicine";
    public static final String note = "note";
    public static final String medDate = "medDate";
    public static final String medTime = "medTime";
}
