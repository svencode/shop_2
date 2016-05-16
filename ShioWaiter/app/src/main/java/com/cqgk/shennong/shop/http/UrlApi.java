package com.cqgk.shennong.shop.http;



/**
 * 接口url地址定义
 *
 * @author duke
 */
public class UrlApi {

    /**
     *
     */
    private static String Service = "http://betam.51xnb.cn";
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
     * 根据条形码查询商品标准信息
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

}
