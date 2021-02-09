package com.example.ble;

public class BLEDevice {

    private final String deviceName;
    private final String macAddress;

    public BLEDevice(String deviceName, String macAddress){
        this.deviceName = deviceName;
        this. macAddress = macAddress;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public String getMacAddress(){
        return macAddress;
    }


}
