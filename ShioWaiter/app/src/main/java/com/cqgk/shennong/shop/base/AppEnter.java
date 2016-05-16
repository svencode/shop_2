package com.cqgk.shennong.shop.base;


import android.support.v4.app.Fragment;

import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.helper.PreferencesHelper;

/**
 * @author duke
 */
public class AppEnter extends BaseApp {



    /**
     * 身份证token:登陆后唯一
     */
    public static String TOKEN = "";
    public static String USERID = "";

    /**
     *
     */
    public static IActivity MainActivity;

    /**
     *
     */
    public static Fragment MainFragment;


    public static String TestCardid="010125200000002";







    /**
     * 退出当前账户
     */
    public static void exitAccount() {
        TOKEN = "";
        USERID="";
        PreferencesHelper.save(Key.TOKEN,"");
        PreferencesHelper.save(Key.USERID,"");
        exitAllActivity();
        NavigationHelper.getInstance().startLoginActivity();
    }


}
