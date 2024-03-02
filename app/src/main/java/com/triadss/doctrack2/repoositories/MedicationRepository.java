package com.triadss.doctrack2.repoositories;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.MedicationModel;
import com.triadss.doctrack2.dto.MedicationDto;

public class MedicationRepository {
    private static final String TAG = "MedicationRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference medicationsCollection = firestore
            .collection(FireStoreCollection.MEDICATIONS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    public void addMedication(MedicationDto medications, MedicationsAddCallback callback) {
        System.out.println("HELLO WORLDDD");
        Log.e(TAG, "A");
        if (user != null) {
            Log.e(TAG, "B");
            medications.setPatientId(user.getUid());

            medicationsCollection
                    .add(medications)
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

    public interface MedicationsAddCallback {
        void onSuccess(String medicationId);

        void onError(String errorMessage);
    }
}
