package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.VitalSignsModel;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.dto.WearableDeviceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class VitalSignsRepository {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference vitalSignsCollection = firestore
            .collection(FireStoreCollection.Vital SignsS_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param vitalSignsDto The DTO (Data Transfer Object) containing patient information.
     */
    public boolean AddVitalSigns(String userId, VitalSignsDto vitalSignsDto)
    {
        try
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // TODO: EDIT THIS
            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
            String dateNow = currentDate.format(formatter);

            Map<String, Object> vitalSigns = new HashMap<>();
            vitalSigns.put(VitalSignsModel.vitalsId, 0);
            vitalSigns.put(VitalSignsModel.patientId, vitalSignsDto.getPatientId());
            vitalSigns.put(VitalSignsModel.bloodPressure, vitalSignsDto.getBloodPressure());
            vitalSigns.put(VitalSignsModel.temperature, vitalSignsDto.getTemperature());
            vitalSigns.put(VitalSignsModel.pulseRate, vitalSignsDto.getPulseRate());
            vitalSigns.put(VitalSignsModel.oxygenLevel, vitalSignsDto.getOxygenLevel());
            vitalSigns.put(VitalSignsModel.weight, vitalSignsDto.getWeight());
            vitalSigns.put(VitalSignsModel.height, vitalSignsDto.getHeight());
            vitalSigns.put(VitalSignsModel.BMI, vitalSignsDto.getBMI());
            // TODO: EDIT THIS
            userRef.set(vitalSigns, SetOptions.merge());
        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

    public boolean AddVitalSignsCallback(VitalSignsDto vitalSignsDto, AddUpdateCallback callback)
    {
        try
        {

            // TODO: EDIT THIS
            Map<String, Object> vitalSigns = new HashMap<>();
            vitalSigns.put(VitalSignsModel.vitalsId, 0);
            vitalSigns.put(VitalSignsModel.patientId, vitalSignsDto.getPatientId());
            vitalSigns.put(VitalSignsModel.bloodPressure, vitalSignsDto.getBloodPressure());
            vitalSigns.put(VitalSignsModel.temperature, vitalSignsDto.getTemperature());
            vitalSigns.put(VitalSignsModel.pulseRate, vitalSignsDto.getPulseRate());
            vitalSigns.put(VitalSignsModel.oxygenLevel, vitalSignsDto.getOxygenLevel());
            vitalSigns.put(VitalSignsModel.weight, vitalSignsDto.getWeight());
            vitalSigns.put(VitalSignsModel.height, vitalSignsDto.getHeight());
            vitalSigns.put(VitalSignsModel.BMI, vitalSignsDto.getBMI());
            // TODO: EDIT THIS
            vitalSignsCollection
                .add(vitalSigns)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Vital Signs added with ID: " + documentReference.getId());
                    callback.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding Vital Signs", e);
                    callback.onError(e.getMessage());
                })
                .addOnFailureListener(e -> {
                    // Error fetching user document from Firestore
                    Log.e(TAG, "Error fetching user document from Firestore", e);
                    callback.onError(e.getMessage());
                });

        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

    public interface AddUpdateCallback {
        void onSuccess(String vitalSignsId);

        void onError(String errorMessage);
    }

}
