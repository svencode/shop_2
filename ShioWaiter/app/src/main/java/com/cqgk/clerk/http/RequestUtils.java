package com.cqgk.clerk.http;

import com.cqgk.clerk.bean.normal.LoginResultBean;

import java.net.URL;

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

}
