package com.cqgk.clerk.bean.normal;

/**
 * Created by duke on 16/7/8.
 */
public class DeviceBindBean {

    /**
     * key : 小票打印机的key
     * deviceId : 小票打印机设备的编号
     * nickName : 小票打印机设备的别名
     */

    private String key;
    private String deviceId;
    private String nickName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
