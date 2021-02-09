package com.example.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.welie.blessed.BluetoothCentral;
import com.welie.blessed.BluetoothPeripheral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.UUID;

import static com.example.ble.MainActivity.bluetoothAdapter;
import static com.example.ble.MainActivity.connectedDevice;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(connectedDevice.getMacAddress());
        


    }









}