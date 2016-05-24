package com.cqgk.clerk.bean.normal;

import java.io.Serializable;
import java.util.HashMap;
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

    private double num;
    private String id;
    private String goodsId;
    private String goodsTitle;
    private String specificationDesc;
    private String logoImg;
    private double retailPrice;
    private double vipPrice;
    private double returnPrice;
    private double price;
    private double userPrice;
    private String barCode;

private HashMap<String,String> photoList;

    public HashMap<String, String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(HashMap<String, String> photoList) {
        this.photoList = photoList;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

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

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
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

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDtlBean that = (ProductDtlBean) o;

        if (Double.compare(that.num, num) != 0) return false;
        if (Double.compare(that.retailPrice, retailPrice) != 0) return false;
        if (Double.compare(that.vipPrice, vipPrice) != 0) return false;
        if (Double.compare(that.returnPrice, returnPrice) != 0) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (Double.compare(that.userPrice, userPrice) != 0) return false;
        if (isAllowedModifyPrice != that.isAllowedModifyPrice) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (goodsId != null ? !goodsId.equals(that.goodsId) : that.goodsId != null) return false;
        if (goodsTitle != null ? !goodsTitle.equals(that.goodsTitle) : that.goodsTitle != null)
            return false;
        if (specificationDesc != null ? !specificationDesc.equals(that.specificationDesc) : that.specificationDesc != null)
            return false;
        if (logoImg != null ? !logoImg.equals(that.logoImg) : that.logoImg != null) return false;
        if (barCode != null ? !barCode.equals(that.barCode) : that.barCode != null) return false;
        if (photoList != null ? !photoList.equals(that.photoList) : that.photoList != null)
            return false;
        return !(photoListImg != null ? !photoListImg.equals(that.photoListImg) : that.photoListImg != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(num);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (goodsId != null ? goodsId.hashCode() : 0);
        result = 31 * result + (goodsTitle != null ? goodsTitle.hashCode() : 0);
        result = 31 * result + (specificationDesc != null ? specificationDesc.hashCode() : 0);
        result = 31 * result + (logoImg != null ? logoImg.hashCode() : 0);
        temp = Double.doubleToLongBits(retailPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(vipPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(returnPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(userPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (barCode != null ? barCode.hashCode() : 0);
        result = 31 * result + (photoList != null ? photoList.hashCode() : 0);
        result = 31 * result + isAllowedModifyPrice;
        result = 31 * result + (photoListImg != null ? photoListImg.hashCode() : 0);
        return result;
    }


    public static ProductDtlBean copy(ProductDtlBean bean){
        ProductDtlBean copybean = new ProductDtlBean();

        copybean.num=bean.getNum();
        copybean.id= bean.id;
        copybean.goodsId = bean.goodsId;
        copybean.goodsTitle = bean.goodsTitle;
        copybean.specificationDesc = bean.specificationDesc;
        copybean.logoImg = bean.logoImg;
        copybean.retailPrice = bean.retailPrice;
        copybean.vipPrice = bean.vipPrice;
        copybean.returnPrice = bean.returnPrice;
        copybean.price = bean.price;
        copybean.userPrice = bean.userPrice;
        copybean.barCode = bean.barCode;
        return copybean;
    }
}
