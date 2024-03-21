package com.triadss.doctrack2.activity.core;

import android.Manifest;
import android.app.ActivityManager;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
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
                String logText = "\nHandled message: " + stuff.getString(MessageKey);
                receivedMessageTextView.append("\n" + logText);
                Log.e(TAG, logText);
                return true;
            }
        });



        // Send sample message on click
        sendMessage.setOnClickListener(v -> {
            String onClickMessage = "I just sent the wearable a message " + sentMessageCounter++;
            Log.e(TAG, "Send message: " + onClickMessage);

            //Use the same path//
            new SenderThread(BluetoothConstants.DataPath, onClickMessage).start();
        });

//        getContext().startService(new Intent(getContext(), MessageService.class));
//
//        boolean checkService = isMyServiceRunning(MessageService.class);



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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String getPhoneNodeId(){
//        NodeApi.GetLocalNodeResult nodes = Wearable.NodeApi.getLocalNode(mGoogleApiClient).await();
//        Node node = nodes.getNode();
//        if (node != null) {
//            String nodeId = node.getId();
//            Log.i(getPackageName(), "Node ID: " + nodeId);
//        } else {
//            Log.e(getPackageName(), "No local node found");
//        }
        return "";
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

                    String nodeId = node.getId();
                    Task<Integer> sendMessageTask =
                    //Send the message//
                    Wearable.getMessageClient(getActivity())
                            .sendMessage(node.getId(), path, message.getBytes())
                            .addOnSuccessListener(new OnSuccessListener() {

                                @Override
                                public void onSuccess(Object o) {
                                    Log.d("NodeID", nodeId);
                                    Log.d("MessageSent", "Message sent successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("MessageSent", "Failed to send message", e);
                                }
                            });;




                    try {
                        //Block on a task and get the result synchronously//
                        Integer result = Tasks.await(sendMessageTask);
                        sendMessage("Sent Sample Message " + sentMessageCounter++);
                        //if the Task fails, then…..//
                    } catch (ExecutionException exception) {
                        //TO DO: Handle the exception//
                        Log.e("DeviceFragmenty - ExecutionException", exception.getMessage());
                    } catch (InterruptedException exception) {
                        //TO DO: Handle the exception//
                        Log.e("DeviceFragmenty - InterruptedException", exception.getMessage());
                    }
                }
            } catch (ExecutionException exception) {
                //TO DO: Handle the exception//
                Log.e("ExecutionException", exception.getMessage());
            } catch (InterruptedException exception) {
                //TO DO: Handle the exception//
                Log.e("InterruptedException", exception.getMessage());
            }
        }
    }
}

