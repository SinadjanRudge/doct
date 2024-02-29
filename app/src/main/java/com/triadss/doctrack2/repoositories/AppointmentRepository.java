package com.triadss.doctrack2.repoositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.AppointmentsModel;
import com.triadss.doctrack2.dto.AppointmentDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.util.Log;
import com.google.firebase.Timestamp;
import java.util.Date;

public class AppointmentRepository {
    private static final String TAG = "AppointmentRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentsCollection = firestore
            .collection(FireStoreCollection.APPOINTMENTS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    //! SAMPLE CODE ON HOW TO USE THESE FUNCTIONS, PLEASE REFER TO THE AppointmentRequest.java fragment
    //! located in the patient/fragment folder

    //* Saves patient's appointment to the database using the values exist in the AppointmentDto type
    public void addAppointment(AppointmentDto appointment, AppointmentAddCallback callback) {
        if (user != null) {
            appointment.setPatientId(user.getUid());

            FirebaseFirestore.getInstance()
                    .collection("users") // Change to your users collection name
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

    //* Will fetch all the appointments in descending order
    public void getAllAppointments(AppointmentFetchCallback callback) {
        appointmentsCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<AppointmentDto> appointments = new ArrayList<>();
                        List<String> appointmentIds = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AppointmentDto appointment = document.toObject(AppointmentDto.class);

                            appointments.add(appointment);
                            appointmentIds.add(document.getId());
                        }
                        callback.onSuccess(appointments, appointmentIds);
                    } else {
                        Log.e(TAG, "Error getting appointments", task.getException());
                        callback.onError(task.getException().getMessage());
                    }
                });

    }


    // will update the patient's appointment details
    public void updateAppointment(String appointmentId, AppointmentDto updatedAppointment, UpdateAppointmentCallback callback) {
        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        // Fetch the existing appointment data
        appointmentRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convert the Firestore document to AppointmentDto
                        AppointmentDto existingAppointment = documentSnapshot.toObject(AppointmentDto.class);

                        // Check if the existingAppointment is not null
                        if (existingAppointment != null) {
                            // Update only the specified fields in the existingAppointment
                            if (updatedAppointment.getNameOfRequester() != null) {
                                existingAppointment.setNameOfRequester(updatedAppointment.getNameOfRequester());
                            }
                            if (updatedAppointment.getPurpose() != null) {
                                existingAppointment.setPurpose(updatedAppointment.getPurpose());
                            }

                            if(updatedAppointment.getDateOfAppointment() != null){
                                existingAppointment.setDateOfAppointment(updatedAppointment.getDateOfAppointment());
                            }
                            if(updatedAppointment.getCreatedAt() != null){
                                existingAppointment.setCreatedAt(updatedAppointment.getCreatedAt());
                            }

                            if(updatedAppointment.getPatientId() != null){
                                existingAppointment.setPatientId(updatedAppointment.getPatientId());
                            }

                            if(updatedAppointment.getStatus() != null){
                                existingAppointment.setStatus(updatedAppointment.getStatus());
                            }

                            // Set the updated data with SetOptions.merge() to update only the specified fields
                            appointmentRef.set(existingAppointment, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Appointment updated successfully");
                                        callback.onSuccess();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error updating appointment", e);
                                        callback.onError(e.getMessage());
                                    });
                        } else {
                            Log.e(TAG, "Failed to convert Firestore document to AppointmentDto");
                            callback.onError("Failed to fetch existing appointment data");
                        }
                    } else {
                        Log.e(TAG, "Appointment document does not exist");
                        callback.onError("Appointment document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching appointment data", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getAppointment(String appointmentId, AppointmentFetchOneCallback callback) {
        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AppointmentDto appointment = documentSnapshot.toObject(AppointmentDto.class);
                        callback.onSuccess(Collections.singletonList(appointment));
                    } else {
                        Log.e(TAG, "Appointment document not found");
                        callback.onError("Appointment document not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching appointment data", e);
                    callback.onError(e.getMessage());
                });
    }

    public void deleteAppointment(String appointmentId, DeleteAppointmentCallback callback) {
        DocumentReference appointmentRef = appointmentsCollection.document(appointmentId);

        appointmentRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Appointment deleted successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting appointment", e);
                    callback.onError(e.getMessage());
                });
    }


    public interface AppointmentAddCallback {
        void onSuccess(String appointmentId);

        void onError(String errorMessage);
    }

    public interface AppointmentFetchCallback {

        void onSuccess(List<AppointmentDto> appointments, List<String> appointmentIds);

        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface AppointmentFetchOneCallback {
        void onSuccess(List<AppointmentDto> appointments);

        void onError(String errorMessage);
    }

    public interface UpdateAppointmentCallback {
        void onSuccess();

        void onError(String errorMessage);
    }
    public interface DeleteAppointmentCallback {
        void onSuccess();

        void onError(String errorMessage);
    }


}
