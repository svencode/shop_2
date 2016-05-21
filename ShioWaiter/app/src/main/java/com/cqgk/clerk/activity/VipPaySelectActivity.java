package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.bean.logicbean.WechatResultBean;
import com.cqgk.clerk.bean.normal.RechargeResultBean;
import com.cqgk.clerk.helper.WXPayHelper;
import com.cqgk.clerk.http.HttpCallBack;
import com.cqgk.clerk.http.RequestUtils;
import com.cqgk.clerk.utils.PayUtil;
import com.cqgk.clerk.view.PricesTextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 会员充值支付
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.vippayselect)
public class VipPaySelectActivity extends BusinessBaseActivity {

    @ViewInject(R.id.paymoney)
    PricesTextView paymoney;

    private static final int PAY_UNION = 0;
    private static final int PAY_ALIPAY = 1;
    private static final int PAY_WEIXIN = 2;
    private static final int PAY_NONGYE = 3;
    private static final int PAY_NONGBAO = 4;


    private int Prv_cb_id;
    private int Cur_cb_id;
    private int cur_pay_type;
    private RechargeResultBean payOrderCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("选择支付方式");


        try {
            payOrderCode = (RechargeResultBean) getIntent().getSerializableExtra("ordercode");
            paymoney.setText(payOrderCode.getPayAmount());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Event(R.id.sel_1)
    private void sel_1_click(CheckBox view) {
        if (Prv_cb_id != 0)
            ((CheckBox) findViewById(Prv_cb_id)).setChecked(false);

        Prv_cb_id = view.getId();
        view.setChecked(true);
        cur_pay_type = PAY_UNION;
    }

    /**
     * 支付宝
     *
     * @param view
     */
    @Event(R.id.sel_2)
    private void sel_2_click(CheckBox view) {
        if (Prv_cb_id != 0)
            ((CheckBox) findViewById(Prv_cb_id)).setChecked(false);

        Prv_cb_id = view.getId();
        view.setChecked(true);
        cur_pay_type = PAY_ALIPAY;


    }

    @Event(R.id.sel_3)
    private void sel_3_click(CheckBox view) {
        if (Prv_cb_id != 0)
            ((CheckBox) findViewById(Prv_cb_id)).setChecked(false);

        Prv_cb_id = view.getId();
        view.setChecked(true);
        cur_pay_type = PAY_WEIXIN;
    }

    @Event(R.id.sel_4)
    private void sel_4_click(CheckBox view) {
        if (Prv_cb_id != 0)
            ((CheckBox) findViewById(Prv_cb_id)).setChecked(false);

        Prv_cb_id = view.getId();
        view.setChecked(true);
        cur_pay_type = PAY_NONGYE;
    }

    @Event(R.id.sel_5)
    private void sel_5_click(CheckBox view) {
        if (Prv_cb_id != 0)
            ((CheckBox) findViewById(Prv_cb_id)).setChecked(false);

        Prv_cb_id = view.getId();
        view.setChecked(true);
        cur_pay_type = PAY_NONGBAO;
    }


    @Event(R.id.paynow)
    private void paynow_click(View view) {
        if (cur_pay_type == PAY_ALIPAY) {
            new PayUtil(this).aliPay(payOrderCode.getPayMsg(),
                    payOrderCode.getPayCode(),
                    payOrderCode.getPayMsg(),
                    payOrderCode.getPayAmount());
        } else if (cur_pay_type == PAY_WEIXIN) {

            RequestUtils.prepareWeixinPay(payOrderCode.getPayCode(),
                    "10", new HttpCallBack<WechatResultBean>() {
                @Override
                public void success(WechatResultBean result,String msg) {
                    WXPayHelper.getInstance(result.getAppid()).genPayReq(
                            result.getAppid(),
                            result.getPartnerid(),
                            result.getPrepayid(),
                            result.getPackageX(),
                            result.getNoncestr(),
                            result.getTimestamp(),
                            result.getSign());
                }

                @Override
                public boolean failure(int state, String msg) {
                    showLongToast(msg);
                    return super.failure(state, msg);
                }
            });
        } else {
            showLongToast("此支付方式暂时不支持");
        }
    }

}
