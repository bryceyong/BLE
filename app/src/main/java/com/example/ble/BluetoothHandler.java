package com.example.ble;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.welie.blessed.BluetoothBytesParser;

import com.welie.blessed.BluetoothCentralManager;
import com.welie.blessed.BluetoothCentralManagerCallback;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;
import static com.example.ble.MainActivity.central;
import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT8;
import static com.welie.blessed.BluetoothBytesParser.bytes2String;


import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import timber.log.Timber;

class BluetoothHandler {
    private final Context context;

    //central callback


    private BluetoothHandler(Context context) {
        this.context = context;

        BluetoothCentralManagerCallback bluetoothCentralManagerCallback = new BluetoothCentralManagerCallback() {
            @Override
            public void onDiscoveredPeripheral(@NotNull BluetoothPeripheral peripheral, @NotNull ScanResult scanResult) {

                Log.d("device", peripheral.getAddress());

            }

        };


    }
}




