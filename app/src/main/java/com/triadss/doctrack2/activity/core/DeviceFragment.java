package com.triadss.doctrack2.activity.core;

import static java.util.Arrays.stream;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triadss.doctrack2.R;
import com.triadss.doctrack2.dto.BluetoothDeviceDto;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BluetoothAdapter bluetoothAdapter;
    private RecyclerView pairedDeviceContainer;
    private RecyclerView scannedDeviceContainer;
    private ArrayList<BluetoothDeviceDto> scannedDevices;

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONNECT_BT = 2;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CONNECT_BT);
                    return;
                }

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                BluetoothDeviceDto newDevice = new BluetoothDeviceDto(deviceName, deviceHardwareAddress);

                if(scannedDevices.stream().anyMatch(existingDevice -> existingDevice.getName() == newDevice.getName()
                    && existingDevice.getAddress() == newDevice.getAddress()))
                {
                    scannedDevices.add(newDevice);
                    updateScannedDevices();
                }
            }
        }
    };

    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Bluetooth
        BluetoothManager bluetoothManager = getContext().getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_no_device, container, false);

        pairedDeviceContainer = rootView.findViewById(R.id.pairedDevices);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        pairedDeviceContainer.setLayoutManager(linearLayoutManager);

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        scannedDevices = new ArrayList();

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(receiver, filter);

        return rootView;
    }

    public void updatePairedDevices() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CONNECT_BT);
            return;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDeviceDto> devices = pairedDevices.stream()
                .map(device -> new BluetoothDeviceDto(device.getName(), device.getAddress()))
                .collect(Collectors.toCollection(ArrayList::new));

        DevicePairedDeviceAdapter adapter = new DevicePairedDeviceAdapter(getContext(), devices);
        pairedDeviceContainer.setAdapter(adapter);
    }

    public void updateScannedDevices() {
        DeviceScannedDeviceAdapter adapter = new DeviceScannedDeviceAdapter(getContext(), scannedDevices);
        scannedDeviceContainer.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        getContext().unregisterReceiver(receiver);
    }
}