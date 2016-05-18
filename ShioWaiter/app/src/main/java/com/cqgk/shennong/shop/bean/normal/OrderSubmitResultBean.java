package com.cqgk.shennong.shop.bean.normal;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by sven on 16/5/18.
 */
public class OrderSubmitResultBean implements Serializable{
    private String totalAmount;
    private String bonus;
    private String code;

    private HashMap<String,String> coupon;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HashMap<String, String> getCoupon() {
        return coupon;
    }

    public void setCoupon(HashMap<String, String> coupon) {
        this.coupon = coupon;
    }
}
