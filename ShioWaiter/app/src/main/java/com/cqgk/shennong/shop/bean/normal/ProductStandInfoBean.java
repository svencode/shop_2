package com.cqgk.shennong.shop.bean.normal;

/**
 * Created by duke on 16/5/17.
 */
public class ProductStandInfoBean {

    /**
     * id :
     * title : 伊利牛奶
     * logoImg : http://fs.51xnb.cn/bbb.jpg
     * retailPrice : 120.0
     * vipPrice : 100.0
     */

    private String id;
    private String title;
    private String logoImg;
    private double retailPrice;
    private double vipPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }
}
