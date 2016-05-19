package com.cqgk.clerk.bean.normal;

import java.io.Serializable;

/**
 * Created by duke on 16/5/16.
 */
public class RechargeResultBean implements Serializable{

    /**
     * payCode : 支付订单号
     * payMsg : 支付信息
     * payAmount : 支付金额
     */

    private String payCode;
    private String payMsg;
    private String payAmount;

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayMsg() {
        return payMsg;
    }

    public void setPayMsg(String payMsg) {
        this.payMsg = payMsg;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }
}
