package com.cqgk.shennong.shop.bean.normal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sven on 16/5/16.
 */
public class GoodListBean {
    private int total;
    private ArrayList<Item> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Item> getList() {
        return list;
    }

    public void setList(ArrayList<Item> list) {
        this.list = list;
    }

    public static class Item implements Serializable{
        private String id;
        private String goodsId;
        private String goodsTitle;
        private String specificationDesc;
        private String logoImg;
        private ArrayList<String> photoListImg;
        private double retailPrice;

        private int num;//选择数量

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

        public ArrayList<String> getPhotoListImg() {
            return photoListImg;
        }

        public void setPhotoListImg(ArrayList<String> photoListImg) {
            this.photoListImg = photoListImg;
        }

        public double getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            if (Double.compare(item.retailPrice, retailPrice) != 0) return false;
            if (num != item.num) return false;
            if (id != null ? !id.equals(item.id) : item.id != null) return false;
            if (goodsId != null ? !goodsId.equals(item.goodsId) : item.goodsId != null)
                return false;
            if (goodsTitle != null ? !goodsTitle.equals(item.goodsTitle) : item.goodsTitle != null)
                return false;
            if (specificationDesc != null ? !specificationDesc.equals(item.specificationDesc) : item.specificationDesc != null)
                return false;
            if (logoImg != null ? !logoImg.equals(item.logoImg) : item.logoImg != null)
                return false;
            return photoListImg != null ? photoListImg.equals(item.photoListImg) : item.photoListImg == null;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = id != null ? id.hashCode() : 0;
            result = 31 * result + (goodsId != null ? goodsId.hashCode() : 0);
            result = 31 * result + (goodsTitle != null ? goodsTitle.hashCode() : 0);
            result = 31 * result + (specificationDesc != null ? specificationDesc.hashCode() : 0);
            result = 31 * result + (logoImg != null ? logoImg.hashCode() : 0);
            result = 31 * result + (photoListImg != null ? photoListImg.hashCode() : 0);
            temp = Double.doubleToLongBits(retailPrice);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + num;
            return result;
        }
    }

}


