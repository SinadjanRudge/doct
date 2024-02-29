package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.MedicationModel;
import com.triadss.doctrack2.dto.MedicationDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MedicationRepository {
    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param medicationDto The DTO (Data Transfer Object) containing patient information.
     */
    public boolean AddMedication(String userId, MedicationDto medicationDto)
    {
        try
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // TODO: EDIT THIS
            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
            String dateNow = currentDate.format(formatter);

            Map<String, Object> medications = new HashMap<>();
            medications.put(MedicationModel.mediId, 0);
            medications.put(MedicationModel.patientId, medicationDto.getPatientId());
            medications.put(MedicationModel.medicine, medicationDto.getMedicine());
            medications.put(MedicationModel.note, medicationDto.getNote());
            medications.put(MedicationModel.timestamp, medicationDto.getTimestamp());
            // TODO: EDIT THIS
            userRef.set(medications, SetOptions.merge());
        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

}
