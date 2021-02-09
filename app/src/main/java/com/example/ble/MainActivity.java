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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int ACCESS_LOCATION_REQUEST = 1;
    private ArrayList<BLEDevice> BLEDeviceList = new ArrayList<BLEDevice>();
    private String deviceName;
    private RecyclerView mRecylerView;
    private BLEAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static BLEDevice connectedDevice;
    public static BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //requesting permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "location permission granted", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }

        //start scan button
        Button startScanButton = findViewById(R.id.startScan);
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking start scan button
                Toast.makeText(getApplicationContext(), "scanning", Toast.LENGTH_LONG).show();
                Log.d("myTag", "scanning");

                //scanning
                bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
                BluetoothLeScanner scanner = bluetoothAdapter.getBluetoothLeScanner();

                if (scanner != null) {
                    scanner.startScan(scanCallback);
                    Log.d("process", "scan started");
                }  else {
                    Log.e("process", "could not get scanner object");
                }

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
                openDeviceActivity();
            }
        });

    }



    //scan callback
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            if(device.getName() == null){
                deviceName = "Unnamed";
            } else {
                deviceName = device.getName();
            }

            int found = 0;
            for(BLEDevice foundDevice : BLEDeviceList){
                if(foundDevice.getMacAddress().equals(device.getAddress())){
                    found =  1;
                }
            }

            if(found == 0){
                BLEDeviceList.add(new BLEDevice(deviceName, device.getAddress()));
                mAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            // Ignore for now
        }

        @Override
        public void onScanFailed(int errorCode) {
            Toast.makeText(getApplicationContext(), "scan failed", Toast.LENGTH_SHORT).show();
            Log.d("device found", "nothing");
        }
    };

    //Request Permission
    private void requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH)){

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_LOCATION_REQUEST);
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST);
        }
    }

    public void openDeviceActivity(){
        Intent intent = new Intent(this, DeviceActivity.class);
        startActivity(intent);
    }



}