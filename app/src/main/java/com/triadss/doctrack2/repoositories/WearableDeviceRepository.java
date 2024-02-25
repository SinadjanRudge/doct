package com.triadss.doctrack2.repoositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.triadss.doctrack2.config.constants.DocTrackConstant;
import com.triadss.doctrack2.config.constants.FireStoreCollection;
import com.triadss.doctrack2.config.model.AppointmentsModel;
import com.triadss.doctrack2.config.model.WearableDeviceModel;
import com.triadss.doctrack2.dto.AppointmentDto;
import com.triadss.doctrack2.dto.WearableDeviceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class WearableDeviceRepository {
    /**
     * Saves user information to Firestore.
     *
     * @param userId     The ID of the user.
     * @param wearableDeviceDto The DTO (Data Transfer Object) containing patient information.
     */
    public boolean AddWearableDevice(String userId, WearableDeviceDto wearableDeviceDto)
    {
        try
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // TODO: EDIT THIS
            DocumentReference userRef = db.collection(FireStoreCollection.USERS_TABLE).document(userId);
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DocTrackConstant.AUDIT_DATE_FORMAT);
            String dateNow = currentDate.format(formatter);

            Map<String, Object> wearableDevices = new HashMap<>();
            wearableDevices.put(WearableDeviceModel.deviceName, wearableDeviceDto.getDeviceName());
            wearableDevices.put(WearableDeviceModel.timeSynced, wearableDeviceDto.getTimeSynced());
            wearableDevices.put(WearableDeviceModel.firmwareVersion, wearableDeviceDto.getFirmwareVersion());
            wearableDevices.put(WearableDeviceModel.appVersion, wearableDeviceDto.getAppVersion());
            wearableDevices.put(WearableDeviceModel.remainingBattery, wearableDeviceDto.getRemainingBattery());
            // TODO: EDIT THIS
            userRef.set(wearableDevices, SetOptions.merge());
        } catch(Exception ex)
        {
            return false;
        }

        return true;
    }

}
