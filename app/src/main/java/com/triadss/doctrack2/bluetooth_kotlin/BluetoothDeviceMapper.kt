package com.triadss.doctrack2.bluetooth_kotlin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain():BluetoothDeviceDomain {

    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}