package com.cqgk.clerk.http;


import com.cqgk.clerk.BuildConfig;

/**
 * 接口url地址定义
 *
 * @author duke
 */
public class UrlApi {

    /**
     *
     */
    private static String Service = BuildConfig.API_HOST;//BuildConfig.API_HOST;//BuildConfig.DEBUG? "http://betam.51xnb.cn": "http://m.51xnb.cn";
    /**  */
    private static String Domain = "/clerk/";

    public static String getService() {
        return Service;
    }

    public static void setService(String service, String port) {
        Service = service + ":" + port;
    }

    public static void setService(String service) {
        Service = service;
    }

    public static String getDomain() {
        return Domain;
    }

    public static void setDomain(String domain) {
        Domain = domain;
    }

    public static String getCompleteUrl(String filePath) {
        return String.format("https://%s/%s", Service, filePath);
    }

    public static String getUrl(String action) {
        return String.format("https://%s%s%s", Service, Domain, action);
    }

    public static String getApiUrl(String action) {
        return String.format("%s%s%s", Service, Domain, action);
    }


    /**
     * 无域url自动加上前域
     *
     * @param url
     * @return
     */
    public static String getRealUrl(String url) {
        if (url == null)
            return "";

        if (url.startsWith("http://")) {
            return url;
        } else {
            return String.format(Service + "%s", url);
        }
    }

    /**
     * 获取url中page的id参数
     *
     * @param url
     * @return
     */
    public static String getUrlLastID(String url) {
        try {
            return url.substring(url.lastIndexOf("/") + 1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * token请求
     */
    public static String url_login = "login/login.do";


    /**
     * 退出登录
     */
    public static String url_logout = "login/logout.do";


    /**
     * 登录随机码
     */
    public static String url_randomcode="login/getVerifyCode.do";

    /**
     * 忘记密码
     */
    public static String url_findPwd="login/findPwd.do";

    /**
     * 文件上传
     */
    public static String url_appUpload="file/appUpload.do";

    /**
     * 新增或更新商品
     */
    public static String url_doSaveGoods="goods/doSaveGoods.do";

    /**
     * 使用关键字查询店小二上传的商品
     */
    public static String url_queryClerkGoodsByKey="goods/queryClerkGoodsByKey.do";

    /**
     * 根据条形码查询商品标准信息(多个)
     */
    public static String url_queryClerkGoodsByBarcode="goods/queryClerkGoodsByBarcode.do";

    /**
     *根据商品ID获取店铺上传商品的详细信息
     */
    public static String url_queryClerkGoodsById="goods/queryClerkGoodsById.do";

    /**
     * 检查会员卡有效性
     */
    public static String url_checkCardStatus="member/checkCardStatus.do";

    /**
     *会员开卡
     */
    public static String url_membercardActivate="member/membercardActivate.do";

    /**
     * 会员开卡充值
     */
    public static String url_recharge="member/recharge.do";

    /**
     * 首页 统计当前店小二账户的今日会员卡 、今日现金、昨日销售 资金流水
     */
    public static String url_incomeStatistics="statistics/incomeStatistics.do ";

    /**
     *订单结算提交
     */
    public static String url_submitOrder="order/submitOrder.do";

    /**
     *查询本店热销商品
     */
    public static String url_queryClerkTopGoodsList="goods/queryClerkTopGoodsList.do";

    /**
     * 挑选商品时根据关键字查询商品
     */
    public static String url_queryClerkGoodsListPageByKeys="goods/queryClerkGoodsListPageByKeys.do";

    /**
     *首页查询店铺名字
     */
    public static String url_queryServiceNickName="statistics/queryServiceNickName.do";

    /**
     *结算界面扫描会员卡时重新计算价格信息
     */
    public static String url_settleScanMemberCard="order/settleScanMemberCard.do";

    /**
     *结算界面扫描现金券时重新计算价格信息
     */
    public static String url_settleScanCashCoupon="order/settleScanCashCoupon.do";

    /**
     * 卡信息
     */
    public static String url_getCardInfo="member/getCardInfo.do";

    /**
     * 查询上传的所有商品
     */
    public static String url_queryClerkGoods="goods/queryClerkGoods.do";

    /**
     * 获取微信支付参数
     */
    public static String url_prepareWeixinPay="payment/prepareWeixinPay.do";


    /**
     * 根据条形码查询商品标准信息
     */
    public static String url_queryGoodsStandardInfo="goods/queryGoodsStandardInfo.do";


    /**
     * 首页广告
     */
    public static String url_queryAdsByPosition="ad/queryAdsByPosition.do";


    /**
     * 结算统一借口
     */
    public static String url_settleRecalculate="order/settleReCalculate.do";

    /**
     * 校验会员卡密码
     */
    public static String url_settleCheckCardPwd="order/settleCheckCardPwd.do";


    /**
     *  接口：deleteClerkGoods
     匿名访问：不支持
     URL：http://betam.51xnb.cn/clerk/goods/deleteClerkGoods.do

     提交数据：
     {
     "goodsId":"12345678"
     }

     返回结果：
     {
     "code": 200,
     "msg": "成功",
     }

     注意：提交的是商品ID，不是规格ID
     */
    public static String url_deleteClerkGoods="goods/deleteGoodsForClerk.do";

    public static String url_device_bind="device/binddevice.do";
    public static String url_device_list="device/device.do";
    public static String url_device_print="device/print.do";
    public static String url_device_del="device/unbinddevice.do";


}
