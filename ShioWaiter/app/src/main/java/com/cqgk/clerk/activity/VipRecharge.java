package com.cqgk.clerk.activity;

import android.os.Bundle;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;

/**
 * 会员充值
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.viprecharge)
public class VipRecharge extends BusinessBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("会员充值");
    }
}
