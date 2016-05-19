package com.cqgk.clerk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cqgk.clerk.bean.logicbean.WechatPayBean;
import com.cqgk.clerk.utils.aliUtil.PayResult;
import com.cqgk.clerk.utils.aliUtil.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.constants.Build;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by sven on 16/5/16.
 */
public class PayUtil {
    private Activity context;

    public PayUtil(Activity context) {
        this.context = context;
    }

    // 商户PID
    public static final String PARTNER = "2088811558700803";
    // 商户收款账号
    public static final String SELLER = "zengxiaotong@cqgk.com.cn";

    public static final String RSA_PRIVATE ="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANZTK1Xu4tiRd1rNKEedks8BdmgcOW2XrcKYYfIywJDpyT7GymlpbcF/4aR9DvtA3zoC1qSElTzya6jX+p4nngLx4U79XSkGcFRnNj2jNUpb1SW0g7czimnK/SKRwETr8+za8vEqPDRgKBy35LL2rSqtKShXPz83e/MeOhE+GqVnAgMBAAECgYBaOwOIlxzrvjo4gRzPIbi36860wAUxbWUbAtphhBpsJ/CwvDJlNJyflT4i6P+QqdwQ6TcCZksKMKlAmUUKpnUv1L07MOCpFQxdXK+2MyZDow4KRS4+BWHnLJSCYjqi69z1F4tnzgpOCK7MKoMX1lezbpDI1lgp/q2SzKud8BELiQJBAPXasNJqiPx99cC6rRLYnn3qLkKvnwrtx4WlmBwrBEPINMkUiQ4nOYr95PeQcj4uDn7k/g+d67pyxQ0Ruv1B+bMCQQDfK2OEn+/Aj1GyfPxAC0FnV9FO9J9d5flv1KRGMrb2BH/r7yHjABMt5Uyf8fRPIqvtpkomXf1eLhD+3bIonuN9AkAi9JMLd8Y+UBJu8pvFADOYp4EoThwIy8IAiIjWCG+0y3Rl2puZ/Y2661pwsILtwFKjTB+rTMLFYagOsaSqeYTtAkEAkaHP1IlGPGOKTa6wMd7mdFjjVuHdabocd3TNKp4HtS2kiMNJWsf+vBGJWkrbQhT/GxtMAhONtrrbDmZ/z3A4RQJARm64Nt1NbyTcH/0kKx8ueIXII2jNyLYWR8xYJUVI25gKJVO6OUO8Y1xuKCF6vRBpNnQbMjWAe0AkMKl35FPzMQ==";

    // 商户私钥，pkcs8格式
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    public void aliPay(String orderName, String orderId, String orderSN, String amount) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    context.finish();
                                }
                            }).show();
            return;
        }
        // 订单
//        String orderInfo = getOrderInfo(dtlData.getOrderinfo().getOrder_sn(), dtlData.getOrderinfo().getOrder_sn(), dtlData.getOrderinfo().getOrder_amount());

        String orderInfo = getOrderInfo(orderName, orderSN, orderId, amount);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(context);

                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    Log.e("msg", "ee--" + resultInfo + "--" + resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(context, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String orderid, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderid + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://betam.51xnb.cn/clerk/payment/notifyAlipayResult.do"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    //--------------------------------微信支付---------------------------------------------------
    public static final String WECHAT_APP_ID = "wxd930ea5d5a258f4f";
    private IWXAPI api;

    public void wechatPay(String orderid) {
        api = WXAPIFactory.createWXAPI(context, WECHAT_APP_ID);
        //TODO 获取支付参数
//        SnacksRequestLogic.wechatPay(context, orderid, new SnacksRequestCallBackT<WechatPayBean>() {
//            @Override
//            public void onSuccessResult(int success, int code, String data, WechatPayBean object) {
//                if (1 == success && null!= object){
//                    wechatPay(object);
//                }else {
//                    AppUtil.showToast("支付失败");
//                }
//            }
//
//            @Override
//            public void onErrorResult(String errorMsg) {
//                AppUtil.showToast("支付失败");
//
//            }
//        });
    }

    //微信支付
    private void wechatPay(WechatPayBean object) {
        api.registerApp(WECHAT_APP_ID);

        if (!checkWechatPaySupported()) {
            return;
        }
        PayReq request = new PayReq();
        request.appId = object.getAction().getAppid();
        request.partnerId = object.getAction().getPartnerid();
        request.prepayId = object.getAction().getPrepayid();
        request.packageValue = object.getAction().getPackageStr();
        request.nonceStr = object.getAction().getNoncestr();
        request.timeStamp = object.getAction().getTimestamp();
        request.sign = object.getAction().getSign();
        api.sendReq(request);
    }

    //检查微信是否支持
    private boolean checkWechatPaySupported() {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            AppUtil.showToast("当前微信版本不支持微信支付");
        }

        return isPaySupported;
    }

}