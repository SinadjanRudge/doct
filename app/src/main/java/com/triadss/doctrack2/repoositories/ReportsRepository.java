package com.triadss.doctrack2.repoositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.dto.MedicationDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ReportsRepository {
    FirebaseAuth mAuth;
    public interface ReportCallback {
        void onReportAddedSuccessfully();
        void onReportFailed(String errorMessage);
    }
    public void addPatientInfoAction(AddPatientDto patientDto, ReportCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the current user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            if (callback != null) {
                callback.onReportFailed("Current user not found");
            }
            return;
        }
        String currentUserId = currentUser.getUid();

        DocumentReference recordRef = db.collection(FireStoreCollection.REPORTS_TABLE).document(currentUserId);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
        String dateNow = currentDate.format(formatter);

        Map<String, Object> recordData = new HashMap<>();
        recordData.put(ReportModel.action, "ADD PATIENT INFO");
        recordData.put(ReportModel.createdBy, currentUserId);
        recordData.put(ReportModel.message, String.format("Patient id:%s name:%s has been created by user id:%s", patientDto.getIdNumber(), patientDto.getFullName(), currentUserId));
        recordData.put(ReportModel.idNumber, patientDto.getIdNumber());
        recordData.put(ReportModel.createdDate, dateNow);
        recordData.put(ReportModel.updatedDate, dateNow);

        // Check if mAuth and mContext are not null
        if (mAuth != null && mAuth.getCurrentUser() != null && callback != null) {
            recordRef.set(recordData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        callback.onReportAddedSuccessfully();
                    })
                    .addOnFailureListener(e -> {
                        callback.onReportFailed("Failed to add report: " + e.getMessage());
                    });
        } else {
            if (callback != null) {
                callback.onReportFailed("Failed to add report");
            }
        }
    }

    public void addPatientMedHistAction(MedicalHistoryDto medicalHistoryDtoDto, AddPatientDto patientDto, ReportCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the current user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            if (callback != null) {
                callback.onReportFailed("Current user not found");
            }
            return;
        }
        String currentUserId = currentUser.getUid();

        DocumentReference recordRef = db.collection(FireStoreCollection.REPORTS_TABLE).document(currentUserId);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
        String dateNow = currentDate.format(formatter);

        Map<String, Object> recordData = new HashMap<>();
        recordData.put(ReportModel.action, "ADD PATIENT MEDICAL HISTORY");
        recordData.put(ReportModel.createdBy, currentUserId);
        recordData.put(ReportModel.message, String.format("Patient id:%s name:%s has been created by user id:%s", medicalHistoryDtoDto.getPatientId(), patientDto.getFullName(), currentUserId));
        recordData.put(ReportModel.idNumber, medicalHistoryDtoDto.getPatientId());
        recordData.put(ReportModel.createdDate, dateNow);
        recordData.put(ReportModel.updatedDate, dateNow);

        // Check if mAuth and mContext are not null
        if (mAuth != null && mAuth.getCurrentUser() != null && callback != null) {
            recordRef.set(recordData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        callback.onReportAddedSuccessfully();
                    })
                    .addOnFailureListener(e -> {
                        callback.onReportFailed("Failed to add report: " + e.getMessage());
                    });
        } else {
            if (callback != null) {
                callback.onReportFailed("Failed to add report");
            }
        }
    }

    public void addPatientMedicationAction(MedicationDto medicationDto, AddPatientDto patientDto, ReportCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the current user's ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            if (callback != null) {
                callback.onReportFailed("Current user not found");
            }
            return;
        }
        String currentUserId = currentUser.getUid();

        DocumentReference recordRef = db.collection(FireStoreCollection.REPORTS_TABLE).document(currentUserId);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
        String dateNow = currentDate.format(formatter);

        Map<String, Object> recordData = new HashMap<>();
        recordData.put(ReportModel.action, "ADD PATIENT MEDICATION");
        recordData.put(ReportModel.createdBy, currentUserId);
        recordData.put(ReportModel.message, String.format("Patient id:%s name:%s has been created by user id:%s", medicationDto.getPatientId(), patientDto.getFullName(), currentUserId));
        recordData.put(ReportModel.idNumber, patientDto.getIdNumber());
        recordData.put(ReportModel.createdDate, dateNow);
        recordData.put(ReportModel.updatedDate, dateNow);

        // Check if mAuth and mContext are not null
        if (mAuth != null && mAuth.getCurrentUser() != null && callback != null) {
            recordRef.set(recordData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        callback.onReportAddedSuccessfully();
                    })
                    .addOnFailureListener(e -> {
                        callback.onReportFailed("Failed to add report: " + e.getMessage());
                    });
        } else {
            if (callback != null) {
                callback.onReportFailed("Failed to add report");
            }
        }
    }
}
