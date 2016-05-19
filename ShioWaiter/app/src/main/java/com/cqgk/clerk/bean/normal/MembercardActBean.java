package com.cqgk.clerk.bean.normal;

/**
 * Created by duke on 16/5/17.
 */
public class MembercardActBean {

    /**
     * card_id : 会员卡号
     * card_name : 会员名
     * card_mobile : 会员手机号码
     * card_idcard : 会员身份证号码
     * card_balance : 余额
     */

    private String card_id;
    private String card_name;
    private String card_mobile;
    private String card_idcard;
    private String card_balance;
    private String user_msg;

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_mobile() {
        return card_mobile;
    }

    public void setCard_mobile(String card_mobile) {
        this.card_mobile = card_mobile;
    }

    public String getCard_idcard() {
        return card_idcard;
    }

    public void setCard_idcard(String card_idcard) {
        this.card_idcard = card_idcard;
    }

    public String getCard_balance() {
        return card_balance;
    }

    public void setCard_balance(String card_balance) {
        this.card_balance = card_balance;
    }
}
