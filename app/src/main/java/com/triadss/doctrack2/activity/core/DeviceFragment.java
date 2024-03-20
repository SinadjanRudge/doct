package com.triadss.doctrack2.activity.core;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.triadss.doctrack2.R;

public class DeviceFragment extends Fragment implements MessageClient.OnMessageReceivedListener {

    private TextView receivedMessageTextView;
    private MessageClient messageClient;
    private String phoneNodeId = "565df7c9"; // Declare phoneNodeId at the class level

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_with_device, container, false);

        receivedMessageTextView = rootView.findViewById(R.id.receivedMessageTextView);

        // Initialize MessageClient
        messageClient = Wearable.getMessageClient(requireContext());
        messageClient.addListener(this);

        // Get the phoneNodeId (Wear OS device ID)
        phoneNodeId = getPhoneNodeId();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the message listener
        messageClient.removeListener(this);
    }



    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String message = new String(messageEvent.getData());
        Log.d("DeviceFragment", "Received message: " + message);

        getActivity().runOnUiThread(() -> {
            // Update UI or perform actions based on the received message
            receivedMessageTextView.setText("Received Message: " + message);
        });

        sendMessageToWearable("/action_response", "Response message from mobile".getBytes());
    }


    private void sendMessageToWearable(String messagePath, byte[] message) {
        // Send message to the Wear OS app
        if (phoneNodeId != null) {
            Wearable.getMessageClient(requireContext())
                    .sendMessage(phoneNodeId, messagePath, message);
        } else {
            Log.e("DeviceFragment", "Phone node ID not available");
        }
    }

    private String getPhoneNodeId() {
        // Check if phoneNodeId is available
        if (phoneNodeId != null) {
            return phoneNodeId;
        } else {
            Log.e("DeviceFragment", "Phone node ID not available");
            return null;
        }
    }
}

