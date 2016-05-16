package com.cqgk.clerk.logic.normal;

import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.utils.CheckUtils;

/**
 * Created by duke on 16/5/16.
 */
public class UserLogic {
    public static boolean isLogin(){
        String token = PreferencesHelper.find(Key.TOKEN,"");
        return CheckUtils.isAvailable(token);
    }
}
