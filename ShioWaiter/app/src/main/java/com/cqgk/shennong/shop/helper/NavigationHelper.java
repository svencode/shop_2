package com.cqgk.shennong.shop.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.cqgk.shennong.shop.activity.CashieringActivity;
import com.cqgk.shennong.shop.activity.InputMoneyActivity;
import com.cqgk.shennong.shop.activity.LoginActivity;
import com.cqgk.shennong.shop.activity.MainActivity;
import com.cqgk.shennong.shop.activity.PaySelectActivity;
import com.cqgk.shennong.shop.activity.PickGoodActivity;
import com.cqgk.shennong.shop.activity.ScanProductActivity;
import com.cqgk.shennong.shop.activity.VipPaySelectActivity;
import com.cqgk.shennong.shop.activity.VipRechargeActivity;
import com.cqgk.shennong.shop.activity.WebViewActivity;
import com.cqgk.shennong.shop.activity.active.ActiveCardActivity;
import com.cqgk.shennong.shop.activity.product.ProductEditActivity;
import com.cqgk.shennong.shop.activity.product.SeachProductActivity;
import com.cqgk.shennong.shop.base.BaseApp;
import com.cqgk.shennong.shop.base.Basic;
import com.cqgk.shennong.shop.bean.normal.GoodListBean;
import com.cqgk.shennong.shop.bean.normal.RechargeResultBean;

import java.io.Serializable;
import java.util.ArrayList;


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
        Intent i = new Intent(getActivity(), PickGoodActivity.class);
        this.startActivity(getActivity(),i);
    }

    public void startPayBill(ArrayList<GoodListBean.Item> myGood){
        Intent i = new Intent(getActivity(), CashieringActivity.class);
        i.putExtra(CashieringActivity.MY_GOOD_LIST,myGood);
        this.startActivity(getActivity(),i);
    }

    public void startVipPaySelect(RechargeResultBean bean){
        Intent i = new Intent(getActivity(), VipPaySelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ordercode", (Serializable) bean);
        i.putExtras(bundle);
        this.startActivity(getActivity(),i);
    }

}
