package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.MedicalHistoryModel;
import com.triadss.doctrack2.dto.MedicalHistoryDto;
import com.triadss.doctrack2.dto.WearableDeviceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MedicalHistoryRepository {
    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param medicalHistoryDto The DTO (Data Transfer Object) containing patient information.
     */
    public boolean AddMedicalHistory(String userId, MedicalHistoryDto medicalHistoryDto)
    {
        try
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // TODO: EDIT THIS
            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
            String dateNow = currentDate.format(formatter);

            Map<String, Object> medicalHistories = new HashMap<>();
            medicalHistories.put(MedicalHistoryModel.medHistId, 0);
            medicalHistories.put(MedicalHistoryModel.patientId, medicalHistoryDto.getPatientId());
            medicalHistories.put(MedicalHistoryModel.pastIllness, medicalHistoryDto.getPastIllness());
            medicalHistories.put(MedicalHistoryModel.prevOperation, medicalHistoryDto.getPrevOperation());
            medicalHistories.put(MedicalHistoryModel.obgyneHist, medicalHistoryDto.getObgyneHist());
            medicalHistories.put(MedicalHistoryModel.familyHist, medicalHistoryDto.getFamilyHist());
            // TODO: EDIT THIS
            userRef.set(medicalHistories, SetOptions.merge());
        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

}
