package com.triadss.doctrack2.bluetooth

import com.triadss.doctrack2.dto.BluetoothDevice

data class BluetoothUiState(
        var scannedDevices: List<BluetoothDevice> = emptyList(),
        var pairedDevices: List<BluetoothDevice> = emptyList()
)