package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cqgk.shennong.shop.R;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**会员充值支付
 * Created by duke on 16/5/11.
 */
@ContentView(R.layout.vippayselect)
public class VipPaySelectActivity extends BusinessBaseActivity {

    @ViewInject(R.id.paymoney)
    TextView paymoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("选择支付方式");
    }


}
