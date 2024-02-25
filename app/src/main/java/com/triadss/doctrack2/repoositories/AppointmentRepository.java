package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.enums.UserRole;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.config.model.AppointmentsModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.AppointmentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;

public class AppointmentRepository {
    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param appointmentDto The DTO (Data Transfer Object) containing patient information.
     */
//    public boolean AddAppointment(String userId, AppointmentDto appointmentDto)
//    {
//        try
//        {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//            // TODO: EDIT THIS
//            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
//            LocalDateTime currentDate = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
//            String dateNow = currentDate.format(formatter);
//
//            Map<String, Object> appointmentData = new HashMap<>();
//            appointmentData.put(AppointmentsModel.nameOfRequester, appointmentDto.getNameOfRequester());
//            appointmentData.put(AppointmentsModel.purpose, appointmentDto.getPurpose());
//            appointmentData.put(AppointmentsModel.dateOfAppointment, appointmentDto.getDateOfAppointment());
//            appointmentData.put(AppointmentsModel.timeOfAppointment, appointmentDto.getTimeOfAppointment());
//            appointmentData.put(AppointmentsModel.status, appointmentDto.getStatus());
//            // TODO: EDIT THIS
//            userRef.set(appointmentData, SetOptions.merge());
//        } catch(Exception ex)
//        {
//            return false;
//        }
//
//        return true;
//    }
    private static final String TAG = "AppointmentRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference appointmentCollection = firestore.collection(FireStoreCollection.APPOINTMENTS_TABLE);

    public void addAppointment(AppointmentDto appointment){
        appointmentCollection.add(appointment).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "Appointment added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error adding appointment", e);
        });

    }

    interface AppointmentAddCallback{
        void onSuccess(String appointmentId);
        void onError(String errorMessage);
    }

    interface AppointmentFetchCallback{
        void onSuccess(List<AppointmentDto> appointments);
        void onError(String errorMessage);
    }

}
