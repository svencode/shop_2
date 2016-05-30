package com.cqgk.clerk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.normal.OrderSubmitResultBean;
import com.cqgk.clerk.helper.NavigationHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 结算后
 * Created by sven on 16/5/17.
 */
@ContentView(R.layout.activity_pay_result)
public class PayResultActivity extends BusinessBaseActivity {
    @ViewInject(R.id.imgIV)
    ImageView imgIV;
    @ViewInject(R.id.orderAmountTV)
    TextView orderAmountTV;
    @ViewInject(R.id.couponTV)
    TextView couponTV;
    @ViewInject(R.id.jfTV)
    TextView jfTV;
    @ViewInject(R.id.payTypeTV)
    TextView payTypeTV;
    @ViewInject(R.id.descTV)
    TextView descTV;

    private OrderSubmitResultBean resultBean;
    private boolean isVipPay;

    public static final String ORDER_RESULT = "order_result";
    public static final String IS_VIP_PAY = "is_vip_pay";

    public static final String BROADCAST_GOON_BUY = "go_on_buy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("支付成功");
        getTitleDelegate().setLeftOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHelper.getInstance().GoHome();
            }
        });
        resultBean = (OrderSubmitResultBean) getIntent().getSerializableExtra(ORDER_RESULT);
        isVipPay = getIntent().getBooleanExtra(IS_VIP_PAY, false);
        showInfo(resultBean);
    }

    private void showInfo(OrderSubmitResultBean bean) {
        orderAmountTV.setText(resultBean.getTotalAmount());
        jfTV.setText("用户获得" + resultBean.getBonus() + "积分");

        String couponStr = "";

        if (null != resultBean.getCoupons()) {
            for (String key : resultBean.getCoupons().keySet()) {
                couponStr = couponStr + resultBean.getCoupons().get(key) + "张" + key + "元\n";
            }
            couponTV.setText(couponStr);
        }


        if (isVipPay) {
            payTypeTV.setText("会员卡支付");
        } else {
            payTypeTV.setText("现金支付");
            descTV.setText("温馨提示：现金支付金额不会进入您的店铺的资金账户中～");

        }
    }

    @Event(R.id.exitBtn)
    private void exit(View view) {
        finish();
        //NavigationHelper.getInstance().GoHome();
    }

    @Event(R.id.goOnBtn)
    private void goOn(View view) {
        finish();
        //NavigationHelper.getInstance().GoHome();
    }
}
