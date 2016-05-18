package com.cqgk.shennong.shop.bean.normal;

import java.util.HashMap;

/**
 * Created by duke on 16/5/18.
 */
public class JIesuanReturnBean {

    /**
     * isAvailable : true
     * faceValue : 抵扣的现金券金额
     * membercard : {"memberCardId":"会员卡ID","name":"张三","phoneNumber":"13512345678","barCode":"NO.123456789","balance":"会员卡余额"}
     * amountMap : {"totalAmount":"订单总价","newGoodsPrice":{"gsid1":"price1","gsid2":"price2"}}
     */

    private boolean isAvailable;
    private String faceValue;
    /**
     * memberCardId : 会员卡ID
     * name : 张三
     * phoneNumber : 13512345678
     * barCode : NO.123456789
     * balance : 会员卡余额
     */

    private MembercardBean membercard;
    /**
     * totalAmount : 订单总价
     * newGoodsPrice : {"gsid1":"price1","gsid2":"price2"}
     */

    private AmountMapBean amountMap;

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public MembercardBean getMembercard() {
        return membercard;
    }

    public void setMembercard(MembercardBean membercard) {
        this.membercard = membercard;
    }

    public AmountMapBean getAmountMap() {
        return amountMap;
    }

    public void setAmountMap(AmountMapBean amountMap) {
        this.amountMap = amountMap;
    }

    public static class MembercardBean {

        private String id;
        private String name;
        private String phoneNumber;
        private String barCode;
        private String balance;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }

    public static class AmountMapBean {
        private String totalAmount;
        private HashMap<String,String> newGoodsPrice;

        public HashMap<String, String> getNewGoodsPrice() {
            return newGoodsPrice;
        }

        public void setNewGoodsPrice(HashMap<String, String> newGoodsPrice) {
            this.newGoodsPrice = newGoodsPrice;
        }

        /**
         * gsid1 : price1
         * gsid2 : price2
         */


        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }


    }
}
