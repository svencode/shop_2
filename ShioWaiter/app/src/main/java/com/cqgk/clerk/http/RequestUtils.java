package com.cqgk.clerk.http;

import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.LoginResultBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.PreferencesHelper;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by duke on 16/5/12.
 */
public class RequestUtils {

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
     * 搜索商品
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
     * 提交订单
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
