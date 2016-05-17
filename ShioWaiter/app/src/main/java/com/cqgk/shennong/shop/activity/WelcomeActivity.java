package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.os.Message;

import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.helper.PreferencesHelper;
import com.cqgk.shennong.shop.logic.normal.UserLogic;
import com.cqgk.shennong.shop.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by duke on 16/5/6.
 */
@ContentView(R.layout.welcome)
public class WelcomeActivity extends BusinessBaseActivity {

    private final int UI_MAIN_TAB = 1;
    private final int UI_LOGIN = 2;
    private static final long SPLASH_DELAY_MILLIS = 2000;//延时3秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestData();
    }


    @Override
    public void requestData() {
        super.requestData();
        sendHandler(getUIHandler(), UI_MAIN_TAB, SPLASH_DELAY_MILLIS);
    }

    @Override
    public void onUIHandleMessage(Message msg) {
        super.onUIHandleMessage(msg);
        switch (msg.what) {
            case UI_MAIN_TAB:
                if(UserLogic.isLogin()){
                    NavigationHelper.getInstance().startMain();
                }else {
                    NavigationHelper.getInstance().startLoginActivity();
                }
                finish();
                break;
        }
    }
}
