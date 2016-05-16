package com.cqgk.shennong.shop.bean.normal;

import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
public class ProductDtlBean {

    /**
     * id : 规格ID
     * goodsId : 商品ID
     * goodsTitle : 商品描述
     * specificationDesc : 规格描述
     * logoImg : 商品主图的URL
     * photoListImg : ["http://fs.51xnb.cn/aaaa.jpg","http://fs.51xnb.cn/bbb.jpg"]
     * retailPrice : 120.0
     * vipPrice : 100.0
     */

    private String id;
    private String goodsId;
    private String goodsTitle;
    private String specificationDesc;
    private String logoImg;
    private double retailPrice;
    private double vipPrice;
    private List<String> photoListImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getSpecificationDesc() {
        return specificationDesc;
    }

    public void setSpecificationDesc(String specificationDesc) {
        this.specificationDesc = specificationDesc;
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

    public List<String> getPhotoListImg() {
        return photoListImg;
    }

    public void setPhotoListImg(List<String> photoListImg) {
        this.photoListImg = photoListImg;
    }
}
