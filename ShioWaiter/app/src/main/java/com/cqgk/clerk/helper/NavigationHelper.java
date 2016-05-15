package com.cqgk.clerk.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.cqgk.clerk.activity.CashieringActivity;
import com.cqgk.clerk.activity.InputMoneyActivity;
import com.cqgk.clerk.activity.LoginActivity;
import com.cqgk.clerk.activity.MainActivity;
import com.cqgk.clerk.activity.PaySelectActivity;
import com.cqgk.clerk.activity.ScanProductActivity;
import com.cqgk.clerk.activity.VipRechargeActivity;
import com.cqgk.clerk.activity.WebViewActivity;
import com.cqgk.clerk.activity.active.ActiveCardActivity;
import com.cqgk.clerk.activity.product.ProductEditActivity;
import com.cqgk.clerk.activity.product.SeachProductActivity;
import com.cqgk.clerk.base.BaseApp;
import com.cqgk.clerk.base.Basic;


/**
 * 功能界面跳转
 * 全部界面之间跳转通过这里定义
 *
 * @author duke
 */
public class NavigationHelper extends Basic {

    private static NavigationHelper instance = null;


    public NavigationHelper() {
    }

    public static NavigationHelper getInstance() {
        if (instance == null) {
            instance = new NavigationHelper();
        }
        return instance;
    }

    private void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private void startActivityForResult(Activity context, Intent intent, int RequestCode) {
        context.startActivityForResult(intent, RequestCode);
    }


    /**
     * 登录界面
     */
    public void startLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (getAppContext() instanceof BaseApp) {
            this.startActivityForResult(getActivity(), i, 0);
        } else {
            this.startActivityForResult(getActivity(), i, 0);
        }

    }

    public void startScanProductActivity(){
        Intent i = new Intent(getActivity(), ScanProductActivity.class);
        this.startActivity(getActivity(), i);
    }

    public void startMain(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startUploadProduct(){
        Intent i = new Intent(getActivity(), ProductEditActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startVipRecharge(){
        Intent i = new Intent(getActivity(), VipRechargeActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startActiveCard(){
        Intent i = new Intent(getActivity(), ActiveCardActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startInputMoney(){
        Intent i = new Intent(getActivity(), InputMoneyActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startPaySelect(){
        Intent i = new Intent(getActivity(), PaySelectActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startWebView(String url) {
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra("url", url);
        this.startActivity(getActivity(), i);
    }

    public void startSearchProduct(){
        Intent i = new Intent(getActivity(), SeachProductActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startCashiering(){
        Intent i = new Intent(getActivity(), CashieringActivity.class);
        this.startActivity(getActivity(),i);
    }

}
