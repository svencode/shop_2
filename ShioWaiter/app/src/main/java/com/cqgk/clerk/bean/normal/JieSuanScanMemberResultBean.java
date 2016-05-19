package com.cqgk.clerk.bean.normal;

/**
 * Created by duke on 16/5/18.
 */
public class JieSuanScanMemberResultBean {

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

    private AmountInfoBean amountInfo;

    public MembercardBean getMembercard() {
        return membercard;
    }

    public void setMembercard(MembercardBean membercard) {
        this.membercard = membercard;
    }

    public AmountInfoBean getAmountInfo() {
        return amountInfo;
    }

    public void setAmountInfo(AmountInfoBean amountInfo) {
        this.amountInfo = amountInfo;
    }

    public static class MembercardBean {
        private String memberCardId;
        private String name;
        private String phoneNumber;
        private String barCode;
        private String balance;

        public String getMemberCardId() {
            return memberCardId;
        }

        public void setMemberCardId(String memberCardId) {
            this.memberCardId = memberCardId;
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

    public static class AmountInfoBean {
        private String totalAmount;
        /**
         * gsid1 : price1
         * gsid2 : price2
         */

        private NewGoodsPriceBean newGoodsPrice;

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public NewGoodsPriceBean getNewGoodsPrice() {
            return newGoodsPrice;
        }

        public void setNewGoodsPrice(NewGoodsPriceBean newGoodsPrice) {
            this.newGoodsPrice = newGoodsPrice;
        }

        public static class NewGoodsPriceBean {
            private String gsid1;
            private String gsid2;

            public String getGsid1() {
                return gsid1;
            }

            public void setGsid1(String gsid1) {
                this.gsid1 = gsid1;
            }

            public String getGsid2() {
                return gsid2;
            }

            public void setGsid2(String gsid2) {
                this.gsid2 = gsid2;
            }
        }
    }
}
