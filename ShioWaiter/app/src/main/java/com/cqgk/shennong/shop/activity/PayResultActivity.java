package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.view.View;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/17.
 */
@ContentView(R.layout.activity_pay_result)
public class PayResultActivity extends BusinessBaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("选择支付方式");
    }

    @Event(R.id.exitBtn)
    private void exit(View view){

    }

    @Event(R.id.goOnBtn)
    private void goOn(View view){

    }
}
