package com.example.ble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.util.Log;

import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;
import com.welie.blessed.GattStatus;

import java.util.UUID;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;
import static com.example.ble.MainActivity.central;
import static com.example.ble.MainActivity.connectedDevice;

public class LedActivity extends AppCompatActivity {

    private static final UUID HRS_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    private static final UUID HEARTRATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);

        BluetoothPeripheral peripheral = central.getPeripheral(connectedDevice.getMacAddress());
        central.connectPeripheral(peripheral, peripheralCallback);
        //peripheral.writeCharacteristic();
;
    }



    private final BluetoothPeripheralCallback peripheralCallback = new BluetoothPeripheralCallback(){
        @Override
        public void onServicesDiscovered(@NonNull BluetoothPeripheral peripheral){
            peripheral.requestMtu(185);
            peripheral.requestConnectionPriority(CONNECTION_PRIORITY_HIGH);

            // Turn on notification for Heart Rate  Service
            if(peripheral.getService(HRS_SERVICE_UUID) != null) {
                BluetoothGattCharacteristic heartrateCharacteristic = peripheral.getCharacteristic(HRS_SERVICE_UUID, HEARTRATE_MEASUREMENT_CHARACTERISTIC_UUID);
                if (heartrateCharacteristic != null) {
                    peripheral.setNotify(heartrateCharacteristic, true);

                }
            }
        }


    };
}