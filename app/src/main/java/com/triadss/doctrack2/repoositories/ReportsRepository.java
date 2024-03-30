package com.triadss.doctrack2.repoositories;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.dto.MedicationDto;
import com.triadss.doctrack2.dto.ReportDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportsRepository {
    private final String TAG = "Reports Repository";
    FirebaseAuth mAuth;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference reportsCollection = firestore
            .collection(FireStoreCollection.REPORTS_TABLE);
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    public interface ReportCallback {
        void onReportAddedSuccessfully();
        void onReportFailed(String errorMessage);
    }

    // HEALTH PROF REPORTS
    public void addHealthProfAcceptedAppointmentReport(String appointmentUid, ReportCallback callback)
    {
        appointmentRepository.getAppointment(appointmentUid, new AppointmentRepository.AppointmentDataFetchCallback() {

            @Override
            public void onSuccess(AppointmentDto appointment) {
                addReport(
                    "ACEEPTED APPOINTMENT",
                           String.format("Accepted appointment of %s at %s",
                                appointment.getNameOfRequester(),
                                DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment()).ToString()),
                           callback);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void addHealthProfRejectedAppointmentReport(String appointmentUid, ReportCallback callback)
    {
        appointmentRepository.getAppointment(appointmentUid, new AppointmentRepository.AppointmentDataFetchCallback() {

            @Override
            public void onSuccess(AppointmentDto appointment) {
                addReport(
                        "REJECTED APPOINTMENT",
                        String.format("Rejected appointment of %s at %s",
                                appointment.getNameOfRequester(),
                                DateTimeDto.ToDateTimeDto(appointment.getDateOfAppointment()).ToString()),
                        callback);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }


    public void addReport(String action, String message, ReportCallback callback)
    {
        if (user == null) {
            if (callback != null) {
                callback.onReportFailed("Current user not found");
            }
            return;
        }
        String currentUserId = user.getUid();

        LocalDateTime currentDate = LocalDateTime.now();
        Timestamp currentTimeStamp  = DateTimeDto.ToDateTimeDto(currentDate).ToTimestamp();

        Map<String, Object> recordData = new HashMap<>();
        recordData.put(ReportModel.action, action);
        recordData.put(ReportModel.createdBy, currentUserId);
        recordData.put(ReportModel.message, message);
        recordData.put(ReportModel.createdDate, currentTimeStamp);
        recordData.put(ReportModel.updatedDate, currentTimeStamp);

        // Check if mAuth and mContext are not null
        if (callback != null) {
            reportsCollection.add(recordData)
                    .addOnSuccessListener(aVoid -> {
                        callback.onReportAddedSuccessfully();
                    })
                    .addOnFailureListener(e -> {
                        callback.onReportFailed("Failed to add report: " + e.getMessage());
                    });
        } else {
            callback.onReportFailed("Failed to add report");
        }
    }

    public void getReportsFromUser(String uid, ReportsFetchCallback callback) {
        if (user != null) {
            reportsCollection
                    .whereEqualTo(ReportModel.createdBy, uid)
                    .orderBy(ReportModel.createdBy, Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<ReportDto> reports = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ReportDto report = document.toObject(ReportDto.class);
                            reports.add(report);
                        }
                        callback.onSuccess(reports);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching medicines", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }
    }

    public void getReportsFromUserFilter(String uid, String find, ReportsFilterCallback callback) {
        if (user != null) {
            reportsCollection
                    .whereEqualTo(ReportModel.createdBy, uid)
                    .orderBy(ReportModel.createdBy, Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<ReportDto> reports = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if(Objects.requireNonNull(document.get("message")).toString().toLowerCase().contains(find.toLowerCase()) || Objects.requireNonNull(document.get("action")).toString().toLowerCase().contains(find.toLowerCase())){
                                ReportDto report = document.toObject(ReportDto.class);
                                reports.add(report);
                            }
                        }
                        callback.onSuccess(reports);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching medicines", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }
    }

    public void getReportsFromDateRange(Timestamp before, Timestamp after, ReportsFetchCallback callback) {
        if (user != null) {
            CollectionReference usersCollection = firestore
                .collection(FireStoreCollection.USERS_TABLE);

            usersCollection
                .get()
                .addOnSuccessListener(userQueryDocumentSnapshots -> {
                        // To get user details
                        List<AddPatientDto> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : userQueryDocumentSnapshots) {
                            AddPatientDto user = document.toObject(AddPatientDto.class);
                            user.setUid(document.getId());
                            users.add(user);
                        }

                        reportsCollection
                            .whereGreaterThanOrEqualTo(ReportModel.createdDate, before)
                            .whereLessThanOrEqualTo(ReportModel.createdDate, after)
                            .orderBy(ReportModel.createdDate, Query.Direction.DESCENDING)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                List<ReportDto> reports = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    ReportDto report = document.toObject(ReportDto.class);
                                    String fullName = users.stream()
                                            .filter(user -> user.getUid().equals(report.getCreatedBy()))
                                            .findFirst().get().getFullName();
                                    report.setCreatedByName(fullName);

                                    reports.add(report);
                                }

                                callback.onSuccess(reports);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Error fetching medicines", e);
                                callback.onError(e.getMessage());
                            });

                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching medicines", e);
                        callback.onError(e.getMessage());
                    });


        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }
    }

    public interface ReportsFetchCallback {
        void onSuccess(List<ReportDto> reports);

        void onError(String errorMessage);
    }
    public interface ReportsFilterCallback {
        void onSuccess(List<ReportDto> reports);

        void onError(String errorMessage);
    }
}
