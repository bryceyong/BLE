package com.example.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class DeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        ImageButton heartbeat = findViewById(R.id.heartbeat);
        heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking start scan button
                openHeartbeatActivity();
            }
        });



    }

    public void openHeartbeatActivity(){
        Intent intent = new Intent(this, HeartbeatActivity.class);
        startActivity(intent);
    }
}