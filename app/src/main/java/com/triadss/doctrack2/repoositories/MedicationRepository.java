package com.triadss.doctrack2.repoositories;

import android.util.Log;

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
import com.triadss.doctrack2.config.constants.MedicationTypeConstants;
import com.triadss.doctrack2.config.model.MedicationModel;
import com.triadss.doctrack2.dto.MedicationDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicationRepository {
    private static final String TAG = "MedicationRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference medicationsCollection = firestore
            .collection(FireStoreCollection.MEDICATIONS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    public void addMedication(MedicationDto medications, MedicationsAddCallback callback) {
        if (user != null) {
            medications.setPatientId(user.getUid());

            Map<String, Object> medicationMap = new HashMap<>();
            medicationMap.put("patientId", medications.getPatientId());
            medicationMap.put("medicine", medications.getMedicine());
            medicationMap.put("note", medications.getNote());
            medicationMap.put("timestamp", medications.getTimestamp());
            medicationMap.put("status", medications.getStatus());

            medicationsCollection
                    .add(medicationMap)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Medication added with ID: " + documentReference.getId());
                        callback.onSuccess(documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding medication", e);
                        callback.onError(e.getMessage());
                    })
                    .addOnFailureListener(e -> {
                        // Error fetching user document from Firestore
                        Log.e(TAG, "Error fetching user document from Firestore", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            Log.e(TAG, "User is null");
            callback.onError("User is null");
        }

    }

    public void getAllMedications(String type, MedicationFetchCallback callback) {
        if (user != null) {
            medicationsCollection
                    .whereEqualTo("patientId", user.getUid())
                     .whereEqualTo("status", type)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<MedicationDto> medications = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            MedicationDto medication = document.toObject(MedicationDto.class);
                            medications.add(medication);
                        }
                        callback.onSuccess(medications);
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

    public interface MedicationsAddCallback {
        void onSuccess(String medicationId);

        void onError(String errorMessage);
    }

    public interface MedicationFetchCallback {
        void onSuccess(List<MedicationDto> medications);

        void onError(String errorMessage);
    }
}
