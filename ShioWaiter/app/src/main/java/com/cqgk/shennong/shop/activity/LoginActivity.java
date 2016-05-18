package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.LoginResultBean;
import com.cqgk.shennong.shop.config.Key;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.helper.PreferencesHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.utils.CheckUtils;
import com.cqgk.shennong.shop.BuildConfig;
import com.cqgk.shennong.shop.R;

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

    @ViewInject(R.id.randomcode)
    Button randomcode;

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("用户登录");
        getTitleDelegate().hideLeftBtn();

        if(BuildConfig.DEBUG){
            mobile.setText("13510371652");
            pwd.setText("123456");
        }

        initView();
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
                NavigationHelper.getInstance().startMain();
                finish();

            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });



    }

    @Event(R.id.randomcode)
    private void randomcode_click(View view){
        if (!CheckUtils.isAvailable(mobile.getText().toString())) {
            showLongToast("手机号码不能为空");
            return;
        }
        RequestUtils.getVerifyCode("0", mobile.getText().toString(), new HttpCallBack<String>() {
            @Override
            public void success(String result) {
                time.start();//开始计时
            }

            @Override
            public boolean failure(int state, String msg) {
                showLongToast(msg);
                return super.failure(state, msg);
            }
        });

    }


    @Event(R.id.findpwd)
    private void findpwd_click(View view){
        NavigationHelper.getInstance().startFindPwd();
    }

    @Override
    public void initView() {
        super.initView();
        time = new TimeCount(60000, 1000);//60秒倒计时
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {//计时完毕时触发
            randomcode.setText("获取随机密码");
            randomcode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            randomcode.setClickable(false);
            randomcode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
