package com.cqgk.clerk.http;

import com.cqgk.clerk.bean.normal.EditBean;
import com.cqgk.clerk.bean.normal.FileUploadResultBean;
import com.cqgk.clerk.bean.normal.HomeBean;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.LoginResultBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.utils.CheckUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    public static void fileUpload(EditBean file, String filename , HttpCallBack<FileUploadResultBean> callBlack ){
        CommonParams params = new CommonParams(UrlApi.getApiUrl(UrlApi.url_appUpload));
        params.setHeader("x_file_name",filename);
        params.setMultipart(true);
        try {
        params.addBodyParameter("file",
                new FileInputStream(new File(file.getPhotoInfo().getPhotoPath())),null,filename);
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
