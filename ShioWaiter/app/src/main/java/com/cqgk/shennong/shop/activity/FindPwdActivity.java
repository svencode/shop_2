package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.utils.CheckUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by duke on 16/5/18.
 */
@ContentView(R.layout.findpwd)
public class FindPwdActivity extends BusinessBaseActivity {


    @ViewInject(R.id.mobile)
    EditText mobile;


    @ViewInject(R.id.pwd)
    EditText smscode;

    @ViewInject(R.id.newpwd)
    EditText newpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Event(R.id.loginbtn)
    private void loginbtn_click(View view) {
        if (!CheckUtils.isAvailable(mobile.getText().toString())) {
            showLongToast("请输入手机号码");
            return;
        }
        if (!CheckUtils.isAvailable(smscode.getText().toString())) {
            showLongToast("请输入验证码");
            return;
        }
        if (!CheckUtils.isAvailable(newpwd.getText().toString())) {
            showLongToast("请输入新密码");
            return;
        }

        RequestUtils.findPwd(mobile.getText().toString(), smscode.getText().toString(),
                newpwd.getText().toString(), new HttpCallBack<String>() {
            @Override
            public void success(String result) {
                showLongToast(result);
                finish();
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });


    }
}
