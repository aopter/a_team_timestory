package com.example.timesequence.entity;

import ohos.distributedschedule.interwork.DeviceInfo;

public class DeviceData  {
    private boolean isChecked;
    private DeviceInfo deviceInfo;

    public DeviceData(boolean isChecked, DeviceInfo deviceInfo) {
        this.isChecked = isChecked;
        this.deviceInfo = deviceInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
