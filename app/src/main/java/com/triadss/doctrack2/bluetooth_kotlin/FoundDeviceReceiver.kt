package com.triadss.doctrack2.bluetooth_kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.bluetooth.BluetoothDevice;
import android.os.Build

class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?)
    {
        when(intent?.action) {
            BluetoothDevice.ACTION_FOUND-> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_NAME,
                        BluetoothDevice::class.java
                    )
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME)
                }

                device?.let(onDeviceFound)
            }
        }
    }
}