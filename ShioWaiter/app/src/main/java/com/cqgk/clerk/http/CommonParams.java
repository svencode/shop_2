package com.cqgk.clerk.http;


import org.xutils.http.RequestParams;

/**
 * Created by duke on 15/12/27.
 */
public class CommonParams extends RequestParams {


    public CommonParams(String url) {
        super(url);
        this.setHeader("Content-Type","application/json");
        this.setConnectTimeout(20 * 1000);
        this.setCacheMaxAge(1000 * 2);

    }


}
