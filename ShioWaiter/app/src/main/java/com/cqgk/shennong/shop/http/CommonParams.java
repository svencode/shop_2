package com.cqgk.shennong.shop.http;


import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.PreferencesHelper;
import com.cqgk.shennong.shop.logic.normal.UserLogic;

import org.xutils.http.RequestParams;

/**
 * Created by duke on 15/12/27.
 */
public class CommonParams extends RequestParams {


    public CommonParams(String url) {
        super(url);
        this.setHeader("Content-Type", "application/json");
        this.setConnectTimeout(20 * 1000);
        this.setCacheMaxAge(1000 * 2);

        if (UserLogic.isLogin()) {
            this.setHeader("x_token", PreferencesHelper.find(Key.TOKEN, ""));
        }

    }


}
