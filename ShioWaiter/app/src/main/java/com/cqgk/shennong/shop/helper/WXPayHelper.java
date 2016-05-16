package com.cqgk.shennong.shop.helper;

/**
 * Created by duke on 15/12/24.
 */

import com.cqgk.shennong.shop.base.Basic;
import com.cqgk.shennong.shop.config.Constant;
import com.cqgk.shennong.shop.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 *
 */
public class WXPayHelper extends Basic {

    private IWXAPI msgApi = null;

    private static WXPayHelper helper = new WXPayHelper();

    public static WXPayHelper getInstance() {
        return helper;
    }

    private String api_key = "";

    PayReq req;
    StringBuffer sb;

    public WXPayHelper() {
        msgApi = WXAPIFactory.createWXAPI(getActivity(), null);
        req = new PayReq();
        msgApi.registerApp(Constant.wxAppid);
        sb = new StringBuffer();
    }

    /**
     *
     * @return
     */
    public Boolean isWxInstall() {
        return msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
    }

    /**
     * 生成32位编码
     *
     * @return string
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 随机数
     *
     * @return
     */
//    private String genNonceStr() {
//        Random random = new Random();
//        return MD5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }

    /**
     * 当前时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    /**
     *发起支付
     * @param appid
     * @param partnerId
     * @param prepayid
     * @param packageStr
     * @param noncestr
     * @param timestamp
     * @param sign
     */
    public void genPayReq(String appid,
                          String partnerId,
                          String prepayid,
                          String packageStr,
                          String noncestr,
                          String timestamp,
                          String sign) {
        req.appId = appid;
        req.partnerId = partnerId;
        req.prepayId = prepayid;
        req.packageValue = packageStr;
        req.nonceStr = noncestr;
        req.timeStamp = timestamp;
        req.sign = sign;

        sb.append("sign\n" + req.sign + "\n\n");

        sendPayReq();

    }

    private void sendPayReq() {

        msgApi.registerApp(Constant.wxAppid);
        msgApi.sendReq(req);
    }

//    private String genAppSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        //sb.append(Constant.API_KEY);
//        sb.append(api_key);
//
//        this.sb.append("sign str\n" + sb.toString() + "\n\n");
//        String appSign = MD5Utils.getMessageDigest(sb.toString().getBytes())
//                .toUpperCase();
//        LogUtil.e("orion-appSign-->", appSign);
//        return appSign;
//    }


}
