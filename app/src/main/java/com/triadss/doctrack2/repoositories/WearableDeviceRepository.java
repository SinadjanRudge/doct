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
import com.triadss.doctrack2.config.model.WearableDeviceModel;
import com.triadss.doctrack2.dto.WearableDeviceDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WearableDeviceRepository {
    private static final String TAG = "WearableDeviceRepository";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference wearablesCollection = firestore.collection(FireStoreCollection.WEARABLEDEVICES_TABLE);
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    public void AddWearableDevice(String userId, WearableDeviceDto wearableDeviceDto, WearableAddCallback callback)
    {
        if(user == null) return;

        wearablesCollection
                .add(wearableDeviceDto)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Wearable device added with ID" + documentReference.getId());
                    callback.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding Wearable Device", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getWearableDevice(String deviceId, GetWearableDeviceCallback callback) {
        if(user == null) return;

        DocumentReference deviceRef = wearablesCollection.document(deviceId);
        deviceRef
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        WearableDeviceDto wearableDevice = documentSnapshot.toObject(WearableDeviceDto.class);
                        callback.onSuccess(wearableDevice);
                    } else {
                        callback.onError("Wearable Device not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting Wearable Device", e);
                    callback.onError(e.getMessage());
                });
    }

    public void getWearableDevices(GetWearableDevicesCallback callback) {
        if(user == null) return;

        wearablesCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Convert query document snapshots to a list of WearableDeviceDto objects
                    // and pass them to the callback
                    // For example:
                    // List<WearableDeviceDto> wearableDevices = new ArrayList<>();
                    // for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    //     WearableDeviceDto wearableDevice = document.toObject(WearableDeviceDto.class);
                    //     wearableDevices.add(wearableDevice);
                    // }
                    // callback.onSuccess(wearableDevices);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting Wearable Devices", e);
                    callback.onError(e.getMessage());
                });
    }

    public void updateWearableDevice(String deviceId, WearableDeviceDto updatedDevice, WearableUpdateCallback callback) {
        if(user == null) return;

        DocumentReference deviceRef = wearablesCollection.document(deviceId);
        deviceRef
                .set(updatedDevice, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Wearable device updated successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating Wearable Device", e);
                    callback.onError(e.getMessage());
                });
    }

    public void deleteWearableDevice(String deviceId, WearableDeleteCallback callback) {
        if(user == null) return;

        DocumentReference deviceRef = wearablesCollection.document(deviceId);
        deviceRef
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Wearable device deleted successfully");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting Wearable Device", e);
                    callback.onError(e.getMessage());
                });
    }

    public interface WearableAddCallback {
        void onSuccess(String medicationId);

        void onError(String errorMessage);
    }
    public interface GetWearableDevicesCallback {
        void onSuccess(List<WearableDeviceDto> wearableDevices);

        void onError(String errorMessage);
    }

    public interface WearableUpdateCallback {
        void onSuccess();

        void onError(String errorMessage);
    }

    public interface WearableDeleteCallback {
        void onSuccess();

        void onError(String errorMessage);
    }
    public interface GetWearableDeviceCallback {
        void onSuccess(WearableDeviceDto wearableDevice);

        void onError(String errorMessage);
    }
}
