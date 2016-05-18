package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.OrderSubmitResultBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sven on 16/5/17.
 */
@ContentView(R.layout.activity_pay_result)
public class PayResultActivity extends BusinessBaseActivity{
    @ViewInject(R.id.imgIV)
    ImageView imgIV;
    @ViewInject(R.id.orderAmountTV)
    TextView orderAmountTV;
    @ViewInject(R.id.couponTV)
    TextView couponTV;
    @ViewInject(R.id.jfTV)
    TextView jfTV;

    private OrderSubmitResultBean resultBean;

    public static final String ORDER_RESULT = "order_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("选择支付方式");
        resultBean = (OrderSubmitResultBean)getIntent().getSerializableExtra(ORDER_RESULT);
        showInfo(resultBean);
    }

    private void showInfo(OrderSubmitResultBean bean){
        orderAmountTV.setText("￥"+resultBean.getTotalAmount());
        jfTV.setText("用户活的"+resultBean.getBonus()+"积分");
    }

    @Event(R.id.exitBtn)
    private void exit(View view){
        System.exit(0);
    }

    @Event(R.id.goOnBtn)
    private void goOn(View view){
        //跳回
    }
}
