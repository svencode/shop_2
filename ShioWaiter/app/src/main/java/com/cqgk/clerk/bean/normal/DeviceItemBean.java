package com.cqgk.clerk.bean.normal;

/**
 * Created by duke on 16/7/8.
 */
public class DeviceItemBean {

    /**
     * deviceKey : 小票打印机1的key
     * deviceName : 小票打印机1的别名
     * deviceSerialNumber : 小票打印机1的设备编号
     */

    private String deviceName;
    private String deviceSerialNumber;


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }
}
