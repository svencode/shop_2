package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.LoginResultBean;
import com.cqgk.clerk.config.Key;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.helper.PreferencesHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.CheckUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by duke on 16/5/6.
 */
@ContentView(R.layout.login)
public class LoginActivity extends BusinessBaseActivity {


    @ViewInject(R.id.loginbtn)
    Button loginbtn;

    @ViewInject(R.id.mobile)
    EditText mobile;

    @ViewInject(R.id.pwd)
    EditText pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("用户登录");
        getTitleDelegate().hideLeftBtn();
    }


    @Event(R.id.loginbtn)
    private void loginbtn_click(View view) {
        if (!CheckUtils.isAvailable(mobile.getText().toString())) {
            showLongToast("手机号码不能为空");
            return;
        }
        if (!CheckUtils.isAvailable(pwd.getText().toString())) {
            showLongToast("密码不能为空");
            return;
        }

        RequestUtils.userlogin(mobile.getText().toString(),
                pwd.getText().toString(),
                new HttpCallBack<LoginResultBean>() {
            @Override
            public void success(LoginResultBean result) {
                if (result == null) {
                    showLongToast("登录失败,稍后再试");
                    return;
                }

                PreferencesHelper.save(Key.TOKEN, result.getToken());
                PreferencesHelper.save(Key.USERID, result.getUserid());

            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });

        NavigationHelper.getInstance().startMain();

    }
}
