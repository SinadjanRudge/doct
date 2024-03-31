package com.triadss.doctrack2.activity.core;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.config.constants.BluetoothConstants;
import com.triadss.doctrack2.dto.VitalSignsDto;
import com.triadss.doctrack2.dto.WearableDeviceDto;
import com.triadss.doctrack2.repoositories.VitalSignsRepository;
import com.triadss.doctrack2.repoositories.WearableDeviceRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceFragment extends Fragment {
    private static final String TAG = "DEVICE";
    private TextView receivedMessageTextView;
    private Button sendMessageBtn, syncButton;
    private int receivedMessageNumber = 1;
    private int sentMessageCounter = 1;
    private Handler handler;
    private Receiver messageReceiver;
    private int count = 0;
    private VitalSignsRepository vitalSignsRepo = new VitalSignsRepository();
    private WearableDeviceRepository wearableDevicesRepo = new WearableDeviceRepository();
    private WearableDeviceDto wearableDeviceDto = new WearableDeviceDto();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    VitalSignsDto vitalSignsDto = new VitalSignsDto();

    //* TextViews for the Vital Signs
    private TextView bloodPressureValue, temperatureValue, spo2Value, pulseRateValue, weightValue, heightValue, BMIValue;

    //* TextViews for the Device Registered
    private TextView lastSyncVal, deviceNameVal, deviceIDVal, isNearbyVal, noDeviceFound;
    private TableLayout deviceRegisteredTable;
    private static boolean checkOnce = false;
    private DataClient.OnDataChangedListener dataListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_with_device, container, false);
        initializeViews(rootView);
        initializeHandlers();
        initializeListeners();
        initVitalSigns();
        startContinuousCheck();
        setupDataClientListener();
        return rootView;
    }

    private void setupDataClientListener() {
        dataListener = new DataClient.OnDataChangedListener() {
            @Override
            public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
                for (DataEvent event : dataEventBuffer) {
                    if (event.getType() == DataEvent.TYPE_CHANGED) {
                        DataItem dataItem = event.getDataItem();
                        if (dataItem.getUri().getPath().equals("/wear_to_mobile")) {
                            DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                            String jsonData = dataMap.getString("jsonData");
                            Log.d(TAG, "Received JSON data: " + jsonData);

                            // Handle the received JSON data here
                            // For example, update UI or perform other actions
                        }
                    }
                }
            }
        };

        Wearable.getDataClient(requireActivity()).addListener(dataListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (dataListener != null) {
            Wearable.getDataClient(requireActivity()).addListener(dataListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dataListener != null) {
            Wearable.getDataClient(requireActivity()).removeListener(dataListener);
        }
    }

    private void startContinuousCheck() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(() -> checkIfPairedDevice(), 0, 10, TimeUnit.SECONDS);
    }

    private void checkIfPairedDevice() {
            Context context = requireContext();
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(context).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    String nodeName = node.getDisplayName();
                    String nodeId = node.getId();
                    boolean isNearby = node.isNearby();

                    Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                    mainThreadHandler.post(() -> {
                        wearableDeviceDto.setDeviceId(nodeId);
                        wearableDeviceDto.setDeviceName(nodeName);
                        wearableDeviceDto.setIsNearby(isNearby);

                        setDeviceRegisteredViews(wearableDeviceDto);

                    });
                }

                showPairedDeviceStatus(nodes.size() == 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        });
    }


    private void showPairedDeviceStatus(boolean isPaired) {
        requireActivity().runOnUiThread(() -> {
            if (isPaired) {
                Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                mainThreadHandler.post(() -> {
                    if(wearableDeviceDto.getIsNearby()){
                        noDeviceFound.setVisibility(View.GONE);
                        deviceRegisteredTable.setVisibility(View.VISIBLE);
                    } else {
                        noDeviceFound.setVisibility(View.VISIBLE);
                        deviceRegisteredTable.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "No Wearable Device Nearby. Searching...", Toast.LENGTH_SHORT).show();
                    }

                });
                if(!checkOnce && wearableDeviceDto.getIsNearby()) {
                    Toast.makeText(requireContext(), "Paired with a smartwatch", Toast.LENGTH_SHORT).show();
                    checkOnce = true;
                }

            } else {
                // No paired device connected
                Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                mainThreadHandler.post(() -> {
                    noDeviceFound.setVisibility(View.VISIBLE);
                    deviceRegisteredTable.setVisibility(View.GONE);
                });
                if(checkOnce) {
                    Toast.makeText(requireContext(), "Please pair a smartwatch", Toast.LENGTH_SHORT).show();
                    checkOnce = false;
                }
            }
        });
    }

    private void initializeViews(View rootView) {
        receivedMessageTextView = rootView.findViewById(R.id.receivedMessageTextView);
        sendMessageBtn = rootView.findViewById(R.id.sendMessageBtn);
        syncButton = rootView.findViewById(R.id.SyncDeviceBtn);

        //* Vital Signs Views
        bloodPressureValue = rootView.findViewById(R.id.BloodPressureValue);
        temperatureValue = rootView.findViewById(R.id.TemperatureValue);
        spo2Value = rootView.findViewById(R.id.Spo2Value);
        pulseRateValue = rootView.findViewById(R.id.PulseRateValue);
        weightValue = rootView.findViewById(R.id.WeightValue);
        heightValue = rootView.findViewById(R.id.HeightValue);
        BMIValue = rootView.findViewById(R.id.BMIValue);

        //* Device Registered
        lastSyncVal = rootView.findViewById(R.id.LastSyncValue);
        deviceNameVal = rootView.findViewById(R.id.DeviceNameValue);
        deviceIDVal = rootView.findViewById(R.id.DeviceIDValue);
        isNearbyVal = rootView.findViewById(R.id.IsNearbyValue);
        deviceRegisteredTable = rootView.findViewById(R.id.DeviceTable);

        noDeviceFound = rootView.findViewById(R.id.NoDeviceFound);
    }

    private void initializeHandlers() {
        handler = new Handler(msg -> {
            Bundle bundle = msg.getData();
            String logText = "\nHandled message: " + bundle.getString(BluetoothConstants.MessageKey);
            receivedMessageTextView.append("\n" + logText);
            Log.e(TAG, logText);
            return true;
        });
    }

    private void initializeListeners() {
        sendMessageBtn.setOnClickListener(v -> {
            String onClickMessage = "I just sent the wearable a message " + sentMessageCounter++;
            Log.e(TAG, "Send message: " + onClickMessage);
            new SenderThread(BluetoothConstants.DataPath, onClickMessage).start();
        });

        syncButton.setOnClickListener(v -> handleSyncButtonClick());
    }

    private void initVitalSigns(){
        try{
            vitalSignsRepo.getVitalSignOfPatient(user.getUid(), new VitalSignsRepository.FetchCallback() {
                @Override
                public void onSuccess(VitalSignsDto vitalSigns) {
                    vitalSignsDto = vitalSigns;
                    setVitalSignsViews(vitalSignsDto);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e(TAG, "Failure in fetching patient's vital signs");
                    Toast.makeText(getContext(), "Sync Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){

        }
    }

    private void setVitalSignsViews(VitalSignsDto vitalSigns){
        bloodPressureValue.setText(vitalSigns.getBloodPressure());
        temperatureValue.setText(vitalSigns.getTemperature() + "");
        spo2Value.setText(vitalSigns.getOxygenLevel() + "");
        pulseRateValue.setText(vitalSigns.getPulseRate() + "");
        weightValue.setText(vitalSigns.getWeight() + "");
        heightValue.setText(vitalSigns.getHeight() + "");
        BMIValue.setText(vitalSigns.getBMI() + "");
    }

    private void setDeviceRegisteredViews(WearableDeviceDto wearableDeviceDto){
        deviceNameVal.setText(wearableDeviceDto.getDeviceName());
        deviceIDVal.setText(wearableDeviceDto.getDeviceId());
        isNearbyVal.setText(wearableDeviceDto.getIsNearby() + "");
    }

    private void handleSyncButtonClick() {
        try {
            if(!wearableDeviceDto.getIsNearby()){
                Toast.makeText(getContext(), "Can't sync. No Device Nearby Found.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getContext(), "Syncing...", Toast.LENGTH_SHORT).show();

            wearableDevicesRepo.getWearableDevice(wearableDeviceDto.getDeviceId(), user.getUid(),new WearableDeviceRepository.GetWearableDeviceCallback() {
                @Override
                public void onSuccess(WearableDeviceDto wearableDevice) {
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {
                    wearableDeviceDto.setOwnerId(user.getUid());
                    wearableDevicesRepo.addWearableDevice(wearableDeviceDto, new WearableDeviceRepository.WearableAddCallback() {
                        @Override
                        public void onSuccess(String medicationId) {
                            Toast.makeText(getContext(), "Device Successfully Registered", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
            });

            vitalSignsRepo.getVitalSignOfPatient(user.getUid(), new VitalSignsRepository.FetchCallback() {
                @Override
                public void onSuccess(VitalSignsDto vitalSigns) {
                    String jsonData = vitalSigns.toJsonData();
                    sendMessage(jsonData);

                    //* TODO Update Last Sync

                    Toast.makeText(getContext(), "Sync Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e(TAG, "Failure in fetching patient's vital signs");
                    Toast.makeText(getContext(), "Sync Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messageReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(messageReceiver);
            messageReceiver = null;
        }
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = "\nI just received a message from the wearable "
                    + receivedMessageNumber++
                    + " \n" + intent.getStringExtra(BluetoothConstants.MessageKey);
            receivedMessageTextView.append(message);
        }
    }

    private void sendMessage(String jsonData) {
        String jsonDataWithCount = jsonData + " Count: " + count;
        PutDataMapRequest dataMap = PutDataMapRequest.create("/mobile_to_wear");
        dataMap.getDataMap().putString("jsonData", jsonDataWithCount);
        PutDataRequest request = dataMap.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(getContext()).putDataItem(request);

        putDataTask.addOnSuccessListener(dataItem -> {
            count++;
            Log.d(TAG, "Data sent successfully");
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to send data", e);
        });
    }

    private class SenderThread extends Thread {
        String path;
        String message;

        SenderThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
            Task<List<Node>> wearableList = Wearable.getNodeClient(getContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    String nodeId = node.getId();
                    Task<Integer> sendMessageTask = Wearable.getMessageClient(getActivity())
                            .sendMessage(node.getId(), path, message.getBytes())
                            .addOnSuccessListener(o -> {
                                Log.d("NodeID", nodeId);
                                Log.d("MessageSent", "Message sent successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("MessageSent", "Failed to send message", e);
                            });
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                        sendMessage("Sent Sample Message " + sentMessageCounter++);
                    } catch (ExecutionException | InterruptedException exception) {
                        Log.e("DeviceFragment - Exception", exception.getMessage());
                    }
                }
            } catch (ExecutionException | InterruptedException exception) {
                Log.e("DeviceFragment - Exception", exception.getMessage());
            }
        }
    }
}
