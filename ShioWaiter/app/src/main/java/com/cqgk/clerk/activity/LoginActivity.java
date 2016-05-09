package com.cqgk.clerk.activity;

import android.os.Bundle;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;

/**
 * Created by duke on 16/5/6.
 */
@ContentView(R.layout.login)
public class LoginActivity extends BusinessBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("用户登录");
    }
}
