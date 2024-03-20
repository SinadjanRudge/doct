package com.triadss.doctrack2.activity.core;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.MainActivity;
import com.triadss.doctrack2.dto.BluetoothDeviceDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeviceFragment extends Fragment implements MessageClient.OnMessageReceivedListener {

    private TextView receivedMessageTextView;
    private String phoneNodeId = "565df7c9"; // Declare phoneNodeId at the class level

    // NEW CODE
    private Handler handler;

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_with_device, container, false);

        receivedMessageTextView = rootView.findViewById(R.id.receivedMessageTextView);
        Button sendMessage = rootView.findViewById(R.id.sendMessageBtn);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle stuff = msg.getData();
                String logText = "\nRetrieved message: " + stuff.getString(MessageKey);
                receivedMessageTextView.append("\n" + logText);
                Log.e(TAG, logText);
                return true;
            }
        });

        // Send sample message on click
        sendMessage.setOnClickListener(v -> {
            String onClickMessage = "I just sent the handheld a message ";
            Log.e(TAG, "Send message: " + onClickMessage);

            //Use the same path//
            new SenderThread(DataPath, onClickMessage).start();
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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


    // NEW CODE
    private static final String TAG = "DEVICE";
    private static final String MessageKey = "MessageText";
    private static final String DataPath = "/my_path";
    private int sentMessageCounter = 1;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        // ON RECEIVE FUNCTION HERE
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "Received message from wearable");
        }
    };

    private void sendMessage(String messageText)
    {
        Bundle bundle = new Bundle();
        bundle.putString(MessageKey, messageText);
        Message msg = handler.obtainMessage();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private class SenderThread extends Thread
    {
        String path;
        String message;
        //Constructor for sending information to the Data Layer//
        SenderThread(String p, String m) {
            path = p;
            message = m;
        }
        public void run() {
            //Retrieve the connected devices, known as nodes//
            Task<List<Node>> wearableList =
                    Wearable.getNodeClient(getContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                    //Send the message//
                    Wearable.getMessageClient(getActivity()).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        //Block on a task and get the result synchronously//
                        Integer result = Tasks.await(sendMessageTask);
                        sendMessage("Sent Sample Message " + sentMessageCounter++);
                        //if the Task fails, then…..//
                    } catch (ExecutionException exception) {
                        //TO DO: Handle the exception//
                    } catch (InterruptedException exception) {
                        //TO DO: Handle the exception//
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO: Handle the exception//
            } catch (InterruptedException exception) {
                //TO DO: Handle the exception//
            }
        }
    }
}

