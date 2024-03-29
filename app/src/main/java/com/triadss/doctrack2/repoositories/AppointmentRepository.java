package com.triadss.doctrack2.repoositories;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.activity.LoginActivity;
import com.triadss.doctrack2.config.constants.AppointmentTypeConstants;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.AppointmentsModel;
import com.triadss.doctrack2.config.model.ReportModel;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.AppointmentDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.triadss.doctrack2.dto.DateTimeDto;
import com.triadss.doctrack2.dto.ReportDto;

import java.util.Date;
import java.util.Map;

public class AppointmentRepository {
    private static final String TAG = "AppointmentRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentsCollection = firestore
            .collection(FireStoreCollection.APPOINTMENTS_TABLE);

    private final CollectionReference reportsCollection = firestore
            .collection(FireStoreCollection.REPORTS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    public void addAppointment(AppointmentDto appointment, AppointmentAddCallback callback) {
        if (user != null) {
            appointment.setPatientId(user.getUid());

            FirebaseFirestore.getInstance()
                    .collection(FireStoreCollection.USERS_TABLE)
                    .document(appointment.getPatientId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // * User document found, retrieve full name
                            String fullName = documentSnapshot.getString("fullName");
                            appointment.setNameOfRequester(fullName);

                            appointment.setCreatedAt(new Timestamp(new Date()));

                            // * Proceed with adding the appointment
                            appointmentsCollection
                                    .add(appointment)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "Appointment added with ID: " + documentReference.getId());
                                        callback.onSuccess(documentReference.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding appointment", e);
                                        callback.onError(e.getMessage());
                                    });
                        } else {
                            // User document not found, handle this case
                            Log.e(TAG, "User document not found in Firestore");
                            callback.onError("User document not found");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error fetching user document from Firestore
                        Log.e(TAG, "Error fetching user document from Firestore", e);
                        callback.onError(e.getMessage());
                    });
        }

    }

    public void getAllAppointments(AppointmentFetchCallback callback) {
        appointmentsCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);
                            //document.getId().toString()
                            appointment.setDocumentId(document.get("status").toString());
                            appointments.add(appointment);
                        }
                        callback.onSuccess(appointments);
                    } else {
                        Log.e(TAG, "Error getting appointments", task.getException());
                        callback.onError(task.getException().getMessage());
                    }
                });

    }
    public void getAllPatientPendingAppointments(String patientUid, AppointmentPatientPendingFetchCallback callback) {
        appointmentsCollection.orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.PENDING)
                .whereEqualTo(AppointmentsModel.patientId, patientUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);

                            appointment.setDocumentId(document.getId().toString());
                            appointments.add(appointment);
                        }
                        callback.onSuccess(appointments);
                    } else {
                        Log.e(TAG, "Error getting appointments", task.getException());
                        callback.onError(task.getException().getMessage());
                    }
                });
    }
    public void getAllPatientStatusAppointments(String patientUid, AppointmentPatientStatusFetchCallback callback) {
        appointmentsCollection.orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                .whereEqualTo(AppointmentsModel.patientId, patientUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.get("status").toString().equals("Canceled") || document.get("status").toString().equals("Completed")) {
                                AppointmentDto appointment = document.toObject(AppointmentDto.class);
                                appointment.setDocumentId(document.getId().toString());
                                appointments.add(appointment);
                            }
                        }
                        callback.onSuccess(appointments);
                    } else {
                        Log.e(TAG, "Error getting appointments", task.getException());
                        callback.onError(task.getException().getMessage());
                    }
                });
    }

    public void getAppointmentsForHealthProf(String healthProfId, AppointmentFetchCallback callback) {
        appointmentsCollection
                .where(Filter.or(
                    Filter.equalTo(AppointmentsModel.healthProfId, healthProfId),
                    Filter.equalTo(AppointmentsModel.status, AppointmentTypeConstants.ONGOING)
                ))
                .orderBy(AppointmentsModel.createdAt, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);
                            appointments.add(appointment);
                        }
                        callback.onSuccess(appointments);
                    } else {
                        Log.e(TAG, "Error getting appointments", task.getException());
                        callback.onError(task.getException().getMessage());
                    }
                });
    }

    public void updateAppointmentSchedule(String appointmentId, DateTimeDto newSchedule, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef
                .update(AppointmentsModel.dateOfAppointment, newSchedule.ToTimestamp())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment schedule updated successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating Appointment schedule", e);
                    callback.onError(e.getMessage());
                });
    }

    public void acceptAppointment(String appointmentId, String healthProfId, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef
                .update(AppointmentsModel.status, AppointmentTypeConstants.PENDING,
                        AppointmentsModel.healthProfId, healthProfId)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment status updated successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating Appointment status", e);
                    callback.onError(e.getMessage());
                });
    }

    public void deleteAppointment(String appointmentId, AppointmentAddCallback callback) {
        if(user == null) return;

        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment delete successfully");
                    callback.onSuccess(appointmentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error delete Appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getOngoingAppointments(AppointmentFetchCallback callback)
    {
        if (user != null) {
            appointmentsCollection
                    .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.ONGOING)
                    .orderBy(AppointmentsModel.dateOfAppointment, Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);
                            appointments.add(appointment);
                            appointment.setUid(document.getId());
                        }
                        callback.onSuccess(appointments);
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

    public void getPendingAppointments(String healthProfId, AppointmentFetchCallback callback)
    {
        if (user != null) {
            Timestamp currentTime = DateTimeDto.GetCurrentTimeStamp();
            appointmentsCollection
                    .whereEqualTo(AppointmentsModel.healthProfId, healthProfId)
                    .whereEqualTo(AppointmentsModel.status, AppointmentTypeConstants.PENDING)
                    .whereGreaterThanOrEqualTo(AppointmentsModel.dateOfAppointment, currentTime)
                    .orderBy(AppointmentsModel.dateOfAppointment, Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);
                            appointments.add(appointment);
                            appointment.setUid(document.getId());
                        }
                        callback.onSuccess(appointments);
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


    public void cancelAppointment(String DocumentId, AppointmentCancelCallback callback) {

        appointmentsCollection
                .document(DocumentId)
                .update("status", "Canceled")
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Appointment added with ID: " + DocumentId);
                    callback.onSuccess(DocumentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void rescheduleAppointment(String DocumentId,Timestamp date,AppointmentRescheduleCallback callback) {

        appointmentsCollection
                .document(DocumentId)
                .update("dateOfAppointment", date)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Appointment added with ID: " + DocumentId);
                    callback.onSuccess(DocumentId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    callback.onError(e.getMessage());
                });
    }

    public void addReport(String DocumentId,String action,ReportCallback callback) {

        DocumentReference reportRef = appointmentsCollection.document(DocumentId);

                reportRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Map<String, Object> data = new HashMap<>();
                            data.put("action", action.toString());
                            data.put("createdBy", document.get("createdAt"));
                            data.put("createdDate", document.get("patientId"));
                            data.put("idNumber", document.getId().toString());
                            data.put("message", "Appointment ID:  " + document.getId() + "  has been  " + action.toString()+"ED");
                            data.put("updatedBy", document.get("patientId").toString());
                            data.put("updatedDate", document.get("createdAt"));
                            reportsCollection
                                    .add(data)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "report added with ID: " + DocumentId);
                                        callback.onSuccess(DocumentId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error adding appointment", e);
                                        callback.onError(e.getMessage());
                                    });
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        callback.onError(task.getException().toString());
                    }
                });
    }

    public interface AppointmentCancelCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }
    public interface AppointmentAddCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

    public interface AppointmentFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentPatientPendingFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentPatientStatusFetchCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentRescheduleCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }
    public interface ReportCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }
}
