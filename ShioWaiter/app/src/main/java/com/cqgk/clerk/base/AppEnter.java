package com.cqgk.clerk.base;


import android.support.v4.app.Fragment;

import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.helper.PreferencesHelper;

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
