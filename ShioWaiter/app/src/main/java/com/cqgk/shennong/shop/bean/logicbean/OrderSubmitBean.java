package com.cqgk.shennong.shop.bean.logicbean;

import java.util.ArrayList;

/**
 * Created by sven on 16/5/17.
 */
public class OrderSubmitBean {
    private String MCID;
    private String CCID;
    private ArrayList<SubmitGood> GOODS;

    public String getMCID() {
        return MCID;
    }

    public void setMCID(String MCID) {
        this.MCID = MCID;
    }

    public String getCCID() {
        return CCID;
    }

    public void setCCID(String CCID) {
        this.CCID = CCID;
    }

    public ArrayList<SubmitGood> getGOODS() {
        return GOODS;
    }

    public void setGOODS(ArrayList<SubmitGood> GOODS) {
        this.GOODS = GOODS;
    }

    public static class SubmitGood{
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
