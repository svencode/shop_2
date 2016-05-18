package com.cqgk.shennong.shop.bean.normal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duke on 16/5/17.
 */
public class ProductDtlBean implements Serializable {

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

    private int num;

    private String id;
    private String goodsId;
    private String goodsTitle;
    private String specificationDesc;
    private String logoImg;
    private double retailPrice;
    private double vipPrice;
    private double price;
    private double userPrice;

    private int isAllowedModifyPrice;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(double userPrice) {
        this.userPrice = userPrice;
    }

    public int getIsAllowedModifyPrice() {
        return isAllowedModifyPrice;
    }

    public void setIsAllowedModifyPrice(int isAllowedModifyPrice) {
        this.isAllowedModifyPrice = isAllowedModifyPrice;
    }
}
