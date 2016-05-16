package com.cqgk.shennong.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqgk.shennong.shop.base.AppEnter;
import com.cqgk.shennong.shop.base.BaseApp;
import com.cqgk.shennong.shop.base.BusinessBaseActivity;
import com.cqgk.shennong.shop.bean.normal.HomeBean;
import com.cqgk.shennong.shop.helper.NavigationHelper;
import com.cqgk.shennong.shop.http.HttpCallBack;
import com.cqgk.shennong.shop.http.RequestUtils;
import com.cqgk.shennong.shop.view.CommonDialogView;
import com.cqgk.shennong.shop.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**首页
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.main)
public class MainActivity extends BusinessBaseActivity {

    @ViewInject(R.id.money_a)
    TextView money_a;


    @ViewInject(R.id.money_b)
    TextView money_b;

    @ViewInject(R.id.money_c)
    TextView money_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTitleDelegate();
        getTitleDelegate().setTitle("店小二");
        getTitleDelegate().hideLeftBtn();
        getTitleDelegate().setRightText("退出");
        getTitleDelegate().setRightOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialogView.show("你要退出店小二账号?", new CommonDialogView.DialogClickListener() {
                    @Override
                    public void doConfirm() {
                        AppEnter.exitAccount();
                    }
                });
            }
        });

        requestData();
    }

    @Override
    public void requestData() {
        super.requestData();
        RequestUtils.homedata(new HttpCallBack<HomeBean>() {
            @Override
            public void success(HomeBean result) {
                if(result==null)
                    return;

                money_a.setText(String.valueOf(result.getTCA()));
                money_b.setText(String.valueOf(result.getTMA()));
                money_c.setText(String.valueOf(result.getYTA()));
            }
        });
    }

    @Event(R.id.activecard)
    private void activecard_click(View view){
        NavigationHelper.getInstance().startActiveCard();
    }

    @Event(R.id.uploadprodct)
    private void uploadprodct_click(View view) {
        NavigationHelper.getInstance().startUploadProduct();
    }


    @Event(R.id.viprecharge)
    private void viprecharge_click(View view) {
        NavigationHelper.getInstance().startVipRecharge();
    }

    @Event(R.id.ad_1)
    private void ad_1_click(View view) {
        NavigationHelper.getInstance().startWebView("http://baidu.com");
    }


    @Event(R.id.moneyrecord)
    private void moneyrecord_click(View view) {
        NavigationHelper.getInstance().startCashiering();
    }



}
