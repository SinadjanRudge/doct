package com.triadss.doctrack2.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.triadss.doctrack2.bluetooth.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain():BluetoothDeviceDomain {

    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}