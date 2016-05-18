package com.cqgk.shennong.shop.bean.normal;

import java.util.List;

/**
 * Created by duke on 16/5/18.
 */
public class JiesuanScanSubmit {

    private String barCode;

    private List<ProductInfoEntity> GOODS;


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public List<ProductInfoEntity> getGOODS() {
        return GOODS;
    }

    public void setGOODS(List<ProductInfoEntity> GOODS) {
        this.GOODS = GOODS;
    }

    public static class ProductInfoEntity {
        private String gsid;
        private String price;
        private String num;

        public String getGsid() {
            return gsid;
        }

        public void setGsid(String gsid) {
            this.gsid = gsid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }


}
