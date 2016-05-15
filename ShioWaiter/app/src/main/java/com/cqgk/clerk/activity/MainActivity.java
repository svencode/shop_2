package com.cqgk.clerk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cqgk.clerk.R;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BaseApp;
import com.cqgk.clerk.base.BusinessBaseActivity;
import com.cqgk.clerk.helper.NavigationHelper;
import com.cqgk.clerk.view.CommonDialogView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**首页
 * Created by duke on 16/5/9.
 */
@ContentView(R.layout.main)
public class MainActivity extends BusinessBaseActivity {



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
