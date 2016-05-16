package com.cqgk.clerk.bean.normal;

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

    public static class Item{
        private String id;
        private String goodsId;
        private String goodsTitle;
        private String specificationDesc;
        private String logoImg;
        private ArrayList<String> photoListImg;
        private double retailPrice;

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
    }

}


