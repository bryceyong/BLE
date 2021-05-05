package com.example.ble;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.welie.blessed.BluetoothCentralManager;
import com.welie.blessed.BluetoothCentralManagerCallback;
import com.welie.blessed.BluetoothPeripheral;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int ACCESS_LOCATION_REQUEST = 1;
    public static ArrayList<BLEDevice> BLEDeviceList = new ArrayList<BLEDevice>();
    private String deviceName;
    private RecyclerView mRecylerView;
    public static BLEAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static BLEDevice connectedDevice;
    public static BluetoothAdapter bluetoothAdapter;
    public static BluetoothCentralManager central;
    public static String connectedDeviceName;
    public static String connectedDeviceMac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        requestLocationPermission();

        //requesting permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION )== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "location permission granted", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }

        //start scan button
        Button startScanButton = findViewById(R.id.startScan);
        Button stopScanButton = findViewById(R.id.stopScan);
        central = new BluetoothCentralManager(getApplicationContext(), bluetoothCentralManagerCallback, new Handler());
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking start scan button
                Toast.makeText(getApplicationContext(), "scanning", Toast.LENGTH_LONG).show();
                Log.d("myTag", "scanning");

                //scanning
                central.scanForPeripherals();
            }
        });

        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking start scan button
                Toast.makeText(getApplicationContext(), "stop scanning", Toast.LENGTH_LONG).show();
                //stop scanning
                central.stopScan();
            }
        });


        mRecylerView = findViewById(R.id.recycerlView);
        mRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new BLEAdapter(BLEDeviceList);
        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BLEAdapter.BLEViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                connectedDevice = BLEDeviceList.get(position);
                connectedDeviceName = connectedDevice.getDeviceName();
                connectedDeviceMac = connectedDevice.getMacAddress();
                central.stopScan();
                openDeviceActivity();
            }
        });

    }


    private final BluetoothCentralManagerCallback bluetoothCentralManagerCallback = new BluetoothCentralManagerCallback() {
        @Override
        public void onDiscoveredPeripheral(BluetoothPeripheral peripheral, ScanResult scanResult) {

            //processing found devices
            Log.d("device", peripheral.getAddress());
            if(peripheral.getName() == null){
                deviceName = "Unnamed";
            } else {
                deviceName = peripheral.getName();
            }

            int found = 0;
            for(BLEDevice foundDevice : BLEDeviceList){
                if(foundDevice.getMacAddress().equals(peripheral.getAddress())){
                    found =  1;
                }
            }

            if(found == 0 && deviceName != "Unnamed"){
                BLEDeviceList.add(new BLEDevice(deviceName, peripheral.getAddress()));
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    //Request Permission
    private void requestLocationPermission(){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION_REQUEST);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, ACCESS_LOCATION_REQUEST);

    }

    public void openDeviceActivity(){
        Intent intent = new Intent(this, HeartbeatActivity.class);
        startActivity(intent);
    }











}