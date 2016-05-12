package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.helper.NavigationHelper;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("用户登录");
    }


    @Event(R.id.loginbtn)
    private void loginbtn_click(View view) {
        NavigationHelper.getInstance().startMain();
    }
}
