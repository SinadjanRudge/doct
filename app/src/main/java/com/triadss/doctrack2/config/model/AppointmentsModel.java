package com.triadss.doctrack2.config.model;

import java.util.Date;

/**
 * This class represents the USER model in FireStore.
 * It contains constants for the property names to avoid misspelling when interacting with FireStore.
 */
public class AppointmentsModel extends AuditModel {
    public static final String nameOfRequester = "nameOfRequester";
    public static final String purpose = "purpose";
    public static final String dateOfAppointment = "dateOfAppointment";
    public static final String timeOfAppointment = "timeOfAppointment";
    public static final String status = "status";
}
