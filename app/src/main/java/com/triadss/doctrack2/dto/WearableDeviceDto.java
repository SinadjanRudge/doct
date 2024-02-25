package com.triadss.doctrack2.dto;

import com.google.type.Date;
import com.google.type.DateTime;

import java.sql.Time;

public class WearableDeviceDto {
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DateTime getTimeSynced() {
        return timeSynced;
    }

    public void setTimeSynced(DateTime timeSynced) {
        this.timeSynced = timeSynced;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getRemainingBattery() {
        return remainingBattery;
    }

    public void setRemainingBattery(int remainingBattery) {
        this.remainingBattery = remainingBattery;
    }

    private String deviceName;
    private DateTime timeSynced;
    private String firmwareVersion;
    private String appVersion;
    private int remainingBattery;
}
