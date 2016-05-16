package com.cqgk.shennong.shop.bean.normal;

/**
 * Created by duke on 16/5/16.
 */
public class HomeBean {

    /**
     * TMA : 今日会员卡金额
     * TCA : 今日现金金额
     * YTA : 昨日销售金额
     */

    private double TMA;
    private double TCA;
    private double YTA;

    public double getTMA() {
        return TMA;
    }

    public void setTMA(double TMA) {
        this.TMA = TMA;
    }

    public double getTCA() {
        return TCA;
    }

    public void setTCA(double TCA) {
        this.TCA = TCA;
    }

    public double getYTA() {
        return YTA;
    }

    public void setYTA(double YTA) {
        this.YTA = YTA;
    }
}
