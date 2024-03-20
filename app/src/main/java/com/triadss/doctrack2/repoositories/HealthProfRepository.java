package com.triadss.doctrack2.repoositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.constants.UserRoleConstants;
import com.triadss.doctrack2.config.model.UserModel;
import com.triadss.doctrack2.dto.AddPatientDto;
import com.triadss.doctrack2.dto.HealthProfDto;
import com.triadss.doctrack2.dto.MedicationDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HealthProfRepository {
    private static final String TAG = "HealthProfRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference healProfCollection = firestore
            .collection(FireStoreCollection.USERS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    //private List<HealthProfDto> healthprofdto;

    //create instance from firestore
    public void addHealthProf(HealthProfDto healthProf, HealthProAddCallback callback) {
        if (user != null) {
            healthProf.setHealthProfid(user.getUid());

            Map<String, Object> healthprofMap = new HashMap<>();
            healthprofMap.put("idNumber", user.getUid());
            healthprofMap.put("fullName", healthProf.getFullName());
            healthprofMap.put("role", healthProf.getPosition());
            healthprofMap.put("userName", healthProf.getUserName());
            healthprofMap.put("password", healthProf.getPassword());
            healthprofMap.put("appointmentID", healthProf.getAppointmentId());
            healthprofMap.put("gender", healthProf.getGender());

            //healthprofdto.add(healthProf);

            healProfCollection
                    .add(healthprofMap)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Health Professional added with ID: " + documentReference.getId());
                        callback.onSuccess(documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding medication", e);
                        callback.onFailure(e.getMessage());
                    })
                    .addOnFailureListener(e -> {
                        // Error fetching user document from Firestore
                        Log.e(TAG, "Error fetching user document from Firestore", e);
                        callback.onFailure(e.getMessage());
                    });
        } else {
            Log.e(TAG, "User is null");
            callback.onFailure("User is null");
        }
    }

    public void getHealthProfList(HealthProListCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .whereEqualTo(UserModel.role, UserRoleConstants.HealthProf)
            .get()
            .addOnCompleteListener(task->{
                if (task.isSuccessful()){
                    List<HealthProfDto> healthProfListDto = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()){
                        String role = document.getString(UserModel.role);
                        if (UserRoleConstants.HealthProf.equals(role)) {
                            HealthProfDto patients = document.toObject(HealthProfDto.class);
                            patients.setHealthProfid(document.getId());
                            healthProfListDto.add(patients);
                        }
                    }
                    callback.onSuccess(healthProfListDto);
                }
                else {
                    callback.onFailure(task.getException().getMessage());
                }
            });
    }
    //create add health proflist function from firestore

    public interface HealthProListCallback
    {
        public void onSuccess(List<HealthProfDto> dto);
        public void onFailure(String errorMessage);

    }
    public interface HealthProAddCallback
    {
        public void onSuccess(String healthProfId);
        public void onFailure(String errorMessage);

    }
}
