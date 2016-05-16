package com.cqgk.shennong.shop.bean.logicbean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sven on 16/5/16.
 */
public class WechatPayBean {
    //    {"method":"weixin","action":{"appid":"wx6e743b65ea10f9c6","noncestr":"42ddyc07j68ebeqowvb2v9pbllonti1p","package":"Sign=WXPay","partnerid":"1299959601","prepayid":"wx201601231403319ab734cbb90979357608","timestamp":"1453529008","sign":"B47DEA46FAA541A05EE954D84361E18D"}}
    private String method;
    private Action action;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public static  class Action{
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageStr;
        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageStr() {
            return packageStr;
        }

        public void setPackageStr(String packageStr) {
            this.packageStr = packageStr;
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
    }
}
