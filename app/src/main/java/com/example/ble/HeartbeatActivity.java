package com.example.ble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.welie.blessed.BluetoothBytesParser;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;
import com.welie.blessed.GattStatus;
import com.welie.blessed.WriteType;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import timber.log.Timber;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_HIGH;
import static com.example.ble.DeviceActivity.user;
import static com.example.ble.MainActivity.central;
import static com.example.ble.MainActivity.connectedDevice;


public class HeartbeatActivity extends AppCompatActivity {

    private static final UUID HRS_SERVICE_UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    private static final UUID HEARTRATE_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");

    public static final UUID HPS_SERVICE_UUID = UUID.fromString("00001823-0000-1000-8000-00805f9b34fb");
    private static final UUID LED_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString("00002A5E-0000-1000-8000-00805f9b34fb");

    private int HRpulse;
    private LineChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartbeat);

        BluetoothPeripheral peripheral = central.getPeripheral(connectedDevice.getMacAddress());
        central.connectPeripheral(peripheral, peripheralCallback);

        mChart = (LineChart) findViewById(R.id.graph);

        // enable description text
        mChart.getDescription().setEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.BLACK);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(65000f);
        leftAxis.setAxisMinimum(45000f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getXAxis().setDrawGridLines(true);
        mChart.setDrawBorders(true);

        Thread graphThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updatePulse();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        Thread yAxisThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                leftAxis.setAxisMaximum(HRpulse + 5000);
                                leftAxis.setAxisMinimum(HRpulse - 5000);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        yAxisThread.start();
        graphThread.start();

        ImageButton green = findViewById(R.id.green);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking heartbeat
                Log.d("Hello", Integer.toString(HRpulse));


                BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(HPS_SERVICE_UUID, LED_MEASUREMENT_CHARACTERISTIC_UUID);
                if(characteristic == null){
                    Log.d("NULL", Integer.toString(HRpulse));
                } else {
                    Log.d("NOT NULL", Integer.toString(HRpulse));
                }
                peripheral.writeCharacteristic(characteristic, new byte[] {0x03}, WriteType.WITH_RESPONSE);


            }
        });

        ImageButton red = findViewById(R.id.red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {//to do after clicking heartbeat
                Log.d("Hello", Integer.toString(HRpulse));


                BluetoothGattCharacteristic characteristic = peripheral.getCharacteristic(HPS_SERVICE_UUID, LED_MEASUREMENT_CHARACTERISTIC_UUID);
                if(characteristic == null){
                    Log.d("NULL", Integer.toString(HRpulse));
                } else {
                    Log.d("NOT NULL", Integer.toString(HRpulse));
                }
                peripheral.writeCharacteristic(characteristic, new byte[] {0x01}, WriteType.WITH_RESPONSE);


            }
        });








    }

    //callback functions
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

        public void onCharacteristicUpdate(@NotNull BluetoothPeripheral peripheral, byte[] value, @NotNull BluetoothGattCharacteristic characteristic, @NotNull GattStatus status){
            //HeartRateMeasurement measurement = new HeartRateMeasurement(value);
            //Log.d("BPM", measurement.pulse.toString());
            //Log.d("got", "something");
            HRpulse = (ByteBuffer.wrap(value).getInt());
            //Log.d("soem value", Integer.toString(HRpulse));



        }
    };

    public void updatePulse(){



        TextView heartrate = findViewById(R.id.heartrate);
        heartrate.setText(Integer.toString(HRpulse));
        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);


            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }


            data.addEntry(new Entry(set.getEntryCount(), HRpulse), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(500);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

        }

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Heart Rate");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }






}