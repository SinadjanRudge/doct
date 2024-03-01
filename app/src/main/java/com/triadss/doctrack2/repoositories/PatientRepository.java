package com.triadss.doctrack2.repoositories;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.healthprof.fragment.PatientFragment;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.DocTrackErrorMessage;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.enums.UserRole;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientRepository {
    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param patientDto The DTO (Data Transfer Object) containing patient information.
     */
    public boolean AddPatient(String userId, AddPatientDto patientDto)
    {
        try
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
            String dateNow = currentDate.format(formatter);

            Map<String, Object> userData = new HashMap<>();
            userData.put(UserModel.role, UserRole.PATIENT);
            userData.put(UserModel.email, patientDto.getEmail());
            userData.put(UserModel.fullName, patientDto.getFullName());
            userData.put(UserModel.address, patientDto.getAddress());
            userData.put(UserModel.phone, patientDto.getPhone());
            userData.put(UserModel.course, patientDto.getCourse());
            userData.put(UserModel.idNumber, patientDto.getIdNumber());
            userData.put(UserModel.createdDate, dateNow);
            userData.put(UserModel.updatedDate, dateNow);
            userRef.set(userData, SetOptions.merge());
        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

    public interface PatientListCallback {
        void onSuccess(List<AddPatientDto> patients);
        void onFailure(String errorMessage);
    }

    public void getPatientList(PatientListCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "PATIENT")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()){
                        List<AddPatientDto> addPatientDtoList = new ArrayList<>();

                        for (QueryDocumentSnapshot document: task.getResult()){
                            String role = document.getString("role");
                            if ("PATIENT".equals(role)) {
                                AddPatientDto patients = document.toObject(AddPatientDto.class);
                                addPatientDtoList.add(patients);
                            }
                        }
                        callback.onSuccess(addPatientDtoList);
                    }
                    else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

}
