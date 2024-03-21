package com.triadss.doctrack2.activity.core;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.triadss.doctrack2.R;
import com.triadss.doctrack2.activity.MainActivity;
import com.triadss.doctrack2.bluetooth.MessageService;
import com.triadss.doctrack2.config.constants.BluetoothConstants;
import com.triadss.doctrack2.dto.BluetoothDeviceDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeviceFragment extends Fragment {

    private TextView receivedMessageTextView;
    private String phoneNodeId = "565df7c9"; // Declare phoneNodeId at the class level
    private int receivedMessageNumber = 1;
    private int sentMessageCounter = 1;

    private static final String TAG = "DEVICE";
    private static final String MessageKey = "MessageText";

    // NEW CODE
    private Handler handler;
    private Receiver messageReceiver;

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
            new SenderThread(BluetoothConstants.DataPath, onClickMessage).start();
        });

        // Use Message Service (Note MessageService does broad cast)
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(messageReceiver, messageFilter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the receiver
        if (messageReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(messageReceiver);
            messageReceiver = null;
        }
    }

    // NEW CODE
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = "\nI just received a message from the wearable " 
                + receivedMessageNumber++
                + " \n" + intent.getStringExtra(BluetoothConstants.MessageKey);
            receivedMessageTextView.append(message);
        }
    }


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

