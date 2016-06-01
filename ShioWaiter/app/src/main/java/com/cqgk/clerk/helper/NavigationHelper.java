package com.cqgk.clerk.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.cqgk.clerk.R;
import com.cqgk.clerk.activity.BarCodeFindCashActivity;
import com.cqgk.clerk.activity.BarCodeFindProductActivity;
import com.cqgk.clerk.activity.CashieringActivity;
import com.cqgk.clerk.activity.CrashLogActivity;
import com.cqgk.clerk.activity.FindPwdActivity;
import com.cqgk.clerk.activity.InputMoneyActivity;
import com.cqgk.clerk.activity.LoginActivity;
import com.cqgk.clerk.activity.MainActivity;
import com.cqgk.clerk.activity.PayResultActivity;
import com.cqgk.clerk.activity.PaySelectActivity;
import com.cqgk.clerk.activity.PickGoodActivity;
import com.cqgk.clerk.activity.ScanProductActivity;
import com.cqgk.clerk.activity.VipPaySelectActivity;
import com.cqgk.clerk.activity.VipRechargeActivity;
import com.cqgk.clerk.activity.WebViewActivity;
import com.cqgk.clerk.activity.active.ActiveCardActivity;
import com.cqgk.clerk.activity.product.ProductEditActivity;
import com.cqgk.clerk.activity.product.SeachProductActivity;
import com.cqgk.clerk.base.AppEnter;
import com.cqgk.clerk.base.BaseApp;
import com.cqgk.clerk.base.Basic;
import com.cqgk.clerk.bean.normal.GoodListBean;
import com.cqgk.clerk.bean.normal.OrderSubmitResultBean;
import com.cqgk.clerk.bean.normal.ProductDtlBean;
import com.cqgk.clerk.bean.normal.RechargeResultBean;

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


    public void GoHome() {
        if (AppEnter.MainActivity != null) {
            MainActivity mainTabActivity = (MainActivity) AppEnter.MainActivity;
            AppEnter.exitAllActivityButOne(mainTabActivity);
        }
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

    public void startUploadProduct(String product_id){
        Intent i = new Intent(getActivity(), ProductEditActivity.class);
        i.putExtra("productid",product_id);
        this.startActivity(getActivity(),i);
    }

    public void startVipRecharge(String card_id){
        Intent i = new Intent(getActivity(), VipRechargeActivity.class);
        i.putExtra("card_id",card_id);
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

    public void startPayBill(ArrayList<ProductDtlBean> myGood){
        Intent i = new Intent(getActivity(), CashieringActivity.class);
        i.putExtra(CashieringActivity.MY_GOOD_LIST,myGood);
        this.startActivityForResult(getActivity(),i,0);
//        this.startActivity(getActivity(),i);
    }

    public void startVipPaySelect(RechargeResultBean bean){
        Intent i = new Intent(getActivity(), VipPaySelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ordercode", (Serializable) bean);
        i.putExtras(bundle);
        this.startActivity(getActivity(),i);
    }


    /**
     * 0-更新商品1-返回商品
     * @param showtype
     */
    public void startBarCodeFind(int showtype){
        Intent i = new Intent(getActivity(), BarCodeFindProductActivity.class);
        i.putExtra("showtype",showtype);
        this.startActivityForResult(getActivity(),i,0);
    }

    public void startOrderResult(OrderSubmitResultBean bean,boolean isVipPay) {
        Intent i = new Intent(getActivity(), PayResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PayResultActivity.ORDER_RESULT, bean);
        bundle.putBoolean(PayResultActivity.IS_VIP_PAY,isVipPay);
        i.putExtras(bundle);
        this.startActivity(getActivity(), i);
    }
    public void startFindPwd(){
        Intent i = new Intent(getActivity(), FindPwdActivity.class);
        this.startActivityForResult(getActivity(),i,0);
    }

    public void startCouponScan(){
        Intent i = new Intent(getActivity(), BarCodeFindCashActivity.class);
        this.startActivityForResult(getActivity(),i,0);
    }

    public void startCrashLog(String content){
        Intent i = new Intent(getActivity(), CrashLogActivity.class);
        i.putExtra("path",content);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivityForResult(getActivity(),i,0);
    }

}
