package com.cqgk.shennong.shop.bean.logicbean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duke on 16/5/17.
 */
public class WechatResultBean {

    /**
     * appid : wxf877fb05305f366e
     * partnerid : 1900000109
     * prepayid : wx201411101639507cbf6ffd8b0779950874
     * package : Sign=WXPay
     * noncestr : 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
     * timestamp : 1412000000
     * sign : C380BEC2BFD727A4B6845133519F3AD6
     * msg : ok
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String msg;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
