package com.cqgk.shennong.shop.http;

import com.cqgk.shennong.shop.bean.normal.CardDtlBean;
import com.cqgk.shennong.shop.bean.normal.EditBean;
import com.cqgk.shennong.shop.bean.normal.FileUploadResultBean;
import com.cqgk.shennong.shop.bean.normal.HomeBean;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.bean.normal.LoginResultBean;
import com.cqgk.shennong.shop.bean.normal.MembercardActBean;
import com.cqgk.shennong.shop.bean.normal.ProductDtlBean;
import com.cqgk.shennong.shop.bean.normal.RechargeResultBean;
import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.PreferencesHelper;
import com.cqgk.shennong.shop.utils.CheckUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duke on 16/5/12.
 */
public class RequestUtils {


    /**
     *
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public static void queryClerkGoodsByKey(String keyword,String pageIndex, String pageSize, HttpCallBack<List<ProductDtlBean>> callBack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoods));
        params.addParameter("keyword",keyword);
        params.addParameter("pageIndex",pageIndex);
        params.addParameter("pageSize",pageSize);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }

    /**
     * 查询上传所有的商品
     * @param pageIndex
     * @param pageSize
     * @param callBack
     */
    public static void allProdct(String pageIndex, String pageSize, HttpCallBack<List<ProductDtlBean>> callBack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoods));
        params.addParameter("pageIndex",pageIndex);
        params.addParameter("pageSize",pageSize);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 会员开卡
     * @param card_id
     * @param card_name
     * @param card_mobile
     * @param card_idcard
     * @param card_password
     * @param callBack
     */
    public static void membercardAct(String card_id,
                                     String card_name,
                                     String card_mobile,
                                     String card_idcard,
                                     String card_password,
                                     HttpCallBack<MembercardActBean> callBack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_membercardActivate));
        params.addParameter("card_id",card_id);
        params.addParameter("card_name",card_name);
        params.addParameter("card_mobile",card_mobile);
        params.addParameter("card_idcard",card_idcard);
        params.addParameter("card_password",card_password);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);

    }

    /**
     * 会员卡充值->获取paycode
     * @param card_id
     * @param amount
     * @param callBack
     */
    public static void vipRecharge(String card_id,String amount,HttpCallBack<RechargeResultBean> callBack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_recharge));
        params.addParameter("card_id",card_id);
        params.addParameter("amount",amount);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBack);
    }


    /**
     * 卡信息
     * @param card_id
     * @param callBlack
     */
    public static void cardInfo(String card_id,HttpCallBack<CardDtlBean> callBlack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_getCardInfo));
        params.addParameter("card_id",card_id);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 检测卡有效性
     * @param card_id
     * @param callBlack
     */
    public static void checkCardState(String card_id,HttpCallBack<String> callBlack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_checkCardStatus));
        params.addParameter("card_id",card_id);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 用户登录
     * @param username
     * @param password
     * @param callBlack
     */
    public static void userlogin(String username, String password, HttpCallBack<LoginResultBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_login));
        params.addParameter("username",username);
        params.addParameter("password",password);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 首页
     * @param callBlack
     */
    public static void homedata(HttpCallBack<HomeBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_incomeStatistics));
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /* 搜索商品
     * @param keyword
     * @param pageIndex
     * @param callBlack
     */
    public static void searchGood(String keyword, int pageIndex, HttpCallBack<GoodListBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkGoodsListPageByKeys));
        if (null == keyword){
            keyword = "";
        }
        params = getLoginParams(params);
        params.addParameter("keyword",keyword);
        params.addParameter("pageIndex",pageIndex);
        params.addParameter("pageSize",20);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * 文件上传
     * @param file
     * @param filename
     * @param callBlack
     */
    public static void fileUpload(String filepath, String filename , HttpCallBack<FileUploadResultBean> callBlack ){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_appUpload));
        params.setHeader("x_file_name",filename);
        params.setMultipart(true);
        try {
        params.addBodyParameter("file",
                new FileInputStream(new File(filepath)),null,filename);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        RequestHelper.sendPost(true, params, callBlack);
    }

    /**
     * {
     "id":"商品ID",
     "barCode":"商品的条形码",
     "title":"商品的名称",
     "retailPrice":"商品零售价",
     "vipPrice":"商品的会员价",
     "logoId":"商品主图文件ID",
     "photoIdList":"商品附图文件ID列表"
     }
     */
    public static void productUpdate(String id,
                                     String barcode,
                                     String title,
                                     String retailPrice,
                                     String vipPrice,
                                     String logoId,
                                     String iphoneIds,
                                     HttpCallBack<String> callBlack){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_doSaveGoods));
        params.addParameter("id", CheckUtils.isAvailable(id)?id:"");
        params.addParameter("barCode",barcode);
        params.addParameter("title",title);
        params.addParameter("retailPrice",retailPrice);
        params.addParameter("vipPrice",vipPrice);
        params.addParameter("logoId",logoId);
        params.addParameter("iphotoIdListd",iphoneIds);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }
     /* 提交订单
     * @param MCID
     * @param CCID
     * @param goods
     * @param callBlack
     */
    public static void submitOrder(String MCID, String CCID, ArrayList<String> goods, HttpCallBack<LoginResultBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_submitOrder));
        params = getLoginParams(params);
        params.addBodyParameter("MCID", MCID);
        params.addBodyParameter("CCID", CCID);
        params.setBodyContent(params.toJSONString());
        RequestHelper.sendPost(true, params, callBlack);
    }


    /**
     * 查询本店热销商品
     * @param callBlack
     */
    public static void queryTopGoodsList(HttpCallBack<GoodListBean> callBlack) {
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_queryClerkTopGoodsList));
        params = getLoginParams(params);
        RequestHelper.sendPost(true, params, callBlack);
    }

    private static CommonParams getLoginParams(CommonParams params){
        params.setHeader("x_token", PreferencesHelper.find(Key.TOKEN,""));
        return params;
    }

}
