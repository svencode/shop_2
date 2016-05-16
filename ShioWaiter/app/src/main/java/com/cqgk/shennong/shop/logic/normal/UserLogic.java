package com.cqgk.shennong.shop.logic.normal;

import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.PreferencesHelper;
import com.cqgk.shennong.shop.utils.CheckUtils;

/**
 * Created by duke on 16/5/16.
 */
public class UserLogic {
    public static boolean isLogin(){
        String token = PreferencesHelper.find(Key.TOKEN,"");
        return CheckUtils.isAvailable(token);
    }
}
