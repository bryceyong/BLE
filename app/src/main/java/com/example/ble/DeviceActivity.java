package com.example.ble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.welie.blessed.BluetoothBytesParser;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;
import com.welie.blessed.GattStatus;

import java.util.UUID;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;
import static com.example.ble.MainActivity.central;
import static com.example.ble.MainActivity.connectedDevice;
import static com.example.ble.MainActivity.connectedDeviceMac;
import static com.example.ble.MainActivity.connectedDeviceName;
import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT8;

public class DeviceActivity extends AppCompatActivity {

    TextView batteryText;
    TextView DeviceText;
    TextView MacText;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        user = new User();
        user.name = "Device0001";

        ImageButton heartbeat = findViewById(R.id.heartbeat);
        heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking heartbeat
                openHeartbeatActivity();
            }
        });

    }






    public void openHeartbeatActivity(){
        Intent intent = new Intent(this, HeartbeatActivity.class);
        startActivity(intent);
    }

    public void openLedActivity(){
        Intent intent = new Intent(this, LedActivity.class);
        startActivity(intent);
    }
}